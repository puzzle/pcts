package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.repository.ExperienceCalculationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExperienceCalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<ExperienceCalculation, ExperienceCalculationRepository, ExperienceCalculationPersistenceService> {

    @Autowired
    ExperienceCalculationPersistenceServiceIT(ExperienceCalculationPersistenceService service) {
        super(service);
    }

    @Override
    ExperienceCalculation getModel() {
        return new ExperienceCalculation(null, CALCULATION_1, EXPERIENCE_2, Relevancy.HIGHLY, "Comment");
    }

    @Override
    List<ExperienceCalculation> getAll() {
        return EXPERIENCE_CALCULATIONS;
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
