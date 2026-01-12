package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.repository.ExperienceCalculationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExperienceCalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<ExperienceCalculation, ExperienceCalculationRepository, ExperienceCalculationPersistenceService> {

    CalculationPersistenceServiceIT calculationServiceIT;
    ExperiencePersistenceServiceIT experienceServiceIT;

    private static final Long EXPERIENCE_CALC_ID_1 = 1L;
    private static final Long EXPERIENCE_CALC_ID_2 = 2L;
    private static final Long EXPERIENCE_CALC_ID_3 = 3L;

    @Autowired
    ExperienceCalculationPersistenceServiceIT(ExperienceCalculationPersistenceService service,
                                              CalculationPersistenceService calculationService,
                                              ExperiencePersistenceService experienceService) {
        super(service);
        this.calculationServiceIT = new CalculationPersistenceServiceIT(calculationService, null, null);
        this.experienceServiceIT = new ExperiencePersistenceServiceIT(experienceService, null, null);
    }

    @Override
    ExperienceCalculation getModel() {
        Calculation calculation = calculationServiceIT.getAll().getFirst();
        Experience experience = experienceServiceIT.getAll().getFirst();

        return new ExperienceCalculation(null, calculation, experience, Relevancy.HIGHLY, "Comment");
    }

    @Override
    List<ExperienceCalculation> getAll() {
        List<Calculation> calculations = calculationServiceIT.getAll();
        List<Experience> experiences = experienceServiceIT.getAll();

        List<ExperienceCalculation> list = new ArrayList<>();

        list
                .add(new ExperienceCalculation(EXPERIENCE_CALC_ID_1,
                                               calculations.get(0),
                                               experiences.get(0),
                                               Relevancy.HIGHLY,
                                               "Comment"));

        list
                .add(new ExperienceCalculation(EXPERIENCE_CALC_ID_2,
                                               calculations.get(1),
                                               experiences.get(0),
                                               Relevancy.LITTLE,
                                               "Comment"));

        list
                .add(new ExperienceCalculation(EXPERIENCE_CALC_ID_3,
                                               calculations.get(0),
                                               experiences.get(1),
                                               Relevancy.LIMITED,
                                               "Comment"));

        return list;
    }

    @DisplayName("Should fetch ExperienceCalculations by calculationId")
    @Transactional
    @Test
    void shouldGetByCalculationId() {
        ExperienceCalculation ec = persistenceService.save(getModel());

        List<ExperienceCalculation> result = persistenceService.getByCalculationId(ec.getCalculation().getId());

        assertThat(result).contains(ec);
    }

    @DisplayName("Should fetch ExperienceCalculations by experienceId")
    @Transactional
    @Test
    void shouldGetByExperienceId() {
        ExperienceCalculation ec = persistenceService.save(getModel());

        List<ExperienceCalculation> result = persistenceService.getByExperienceId(ec.getExperience().getId());

        assertThat(result).contains(ec);
    }

    @DisplayName("Should save and retrieve ExperienceCalculation correctly")
    @Transactional
    @Test
    void shouldSaveAndRetrieve() {
        ExperienceCalculation ec = getModel();
        ExperienceCalculation saved = persistenceService.save(ec);

        assertEquals(ec.getCalculation(), saved.getCalculation());
        assertEquals(ec.getExperience(), saved.getExperience());
        assertEquals(ec.getRelevancy(), saved.getRelevancy());
        assertEquals(ec.getComment(), saved.getComment());
        assertThat(persistenceService.getAll()).contains(saved);
    }

    @DisplayName("Should delete all ExperienceCalculations by IDs in batch")
    @Transactional
    @Test
    void shouldDeleteAllByIdInBatch() {
        ExperienceCalculation ec1 = persistenceService.save(getAll().get(0));
        ExperienceCalculation ec2 = persistenceService.save(getAll().get(1));
        ExperienceCalculation ec3 = persistenceService.save(getAll().get(2));

        List<Long> idsToDelete = List.of(ec1.getId(), ec3.getId());

        persistenceService.deleteAllByIdInBatch(idsToDelete);

        List<ExperienceCalculation> remaining = persistenceService.getAll();

        assertThat(remaining).hasSize(1);
        assertThat(remaining.getFirst().getId()).isEqualTo(ec2.getId());
    }

}
