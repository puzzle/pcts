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
        Calculation calculation = calculationServiceIT.getAll().get(0);
        Experience experience = experienceServiceIT.getAll().get(0);

        return new ExperienceCalculation(null, calculation, experience, Relevancy.HIGHLY);
    }

    @Override
    List<ExperienceCalculation> getAll() {
        Calculation calculation1 = calculationServiceIT.getAll().get(0);
        Calculation calculation2 = calculationServiceIT.getAll().get(1);

        Experience experience1 = experienceServiceIT.getAll().get(0);
        Experience experience2 = experienceServiceIT.getAll().get(1);
        List<ExperienceCalculation> experienceCalculationList = new ArrayList<>();
        experienceCalculationList.add(new ExperienceCalculation(1L, calculation1, experience1, Relevancy.HIGHLY));
        experienceCalculationList.add(new ExperienceCalculation(1L, calculation2, experience1, Relevancy.LITTLE));
        experienceCalculationList.add(new ExperienceCalculation(1L, calculation1, experience2, Relevancy.LIMITED));

        return experienceCalculationList;
    }

    @DisplayName("Should fetch ExperienceCalculations by calculationId")
    @Transactional
    @Test
    void shouldGetByCalculationId() {
        ExperienceCalculation ec = service.save(getModel());
        List<ExperienceCalculation> result = service.getByCalculationId(ec.getCalculation().getId());

        assertThat(result).contains(ec);
    }

    @DisplayName("Should fetch ExperienceCalculations by experienceId")
    @Transactional
    @Test
    void shouldGetByExperienceId() {
        ExperienceCalculation ec = service.save(getModel());
        List<ExperienceCalculation> result = service.getByExperienceId(ec.getExperience().getId());

        assertThat(result).contains(ec);
    }

    @DisplayName("Should save and retrieve ExperienceCalculation correctly")
    @Transactional
    @Test
    void shouldSaveAndRetrieve() {
        ExperienceCalculation ec = getModel();
        ExperienceCalculation saved = service.save(ec);

        assertEquals(ec.getCalculation(), saved.getCalculation());
        assertEquals(ec.getExperience(), saved.getExperience());
        assertEquals(ec.getRelevancy(), saved.getRelevancy());
        assertThat(service.getAll()).contains(saved);
    }
}
