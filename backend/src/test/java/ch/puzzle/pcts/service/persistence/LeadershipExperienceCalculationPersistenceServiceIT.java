package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.repository.LeadershipExperienceCalculationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LeadershipExperienceCalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<LeadershipExperienceCalculation, LeadershipExperienceCalculationRepository, LeadershipExperienceCalculationPersistenceService> {

    @Autowired
    LeadershipExperienceCalculationPersistenceServiceIT(LeadershipExperienceCalculationPersistenceService persistenceService) {
        super(persistenceService);
    }

    @Override
    LeadershipExperienceCalculation getModel() {
        return new LeadershipExperienceCalculation(null, CALCULATION_1, LEADERSHIP_EXPERIENCE_1);
    }

    @Override
    List<LeadershipExperienceCalculation> getAll() {
        return LEADERSHIP_EXPERIENCE_CALCULATIONS;
    }

    @DisplayName("Should fetch LeadershipExperienceCalculations by LeadershipExperienceId")
    @Transactional
    @Test
    void shouldGetByLeadershipExperienceId() {
        LeadershipExperienceCalculation lc = persistenceService.save(getModel());

        List<LeadershipExperienceCalculation> result = persistenceService
                .getByLeadershipExperienceId(lc.getLeadershipExperience().getId());

        assertThat(result).contains(lc);
    }

    @DisplayName("Should save and retrieve LeadershipExperienceCalculation correctly")
    @Transactional
    @Test
    void shouldSaveAndRetrieve() {
        LeadershipExperienceCalculation lc = getModel();
        LeadershipExperienceCalculation saved = persistenceService.save(lc);

        assertEquals(lc.getCalculation(), saved.getCalculation());
        assertEquals(lc.getLeadershipExperience(), saved.getLeadershipExperience());
        assertThat(persistenceService.getAll()).contains(saved);
    }

    @DisplayName("Should delete all LeadershipExperienceCalculations by IDs in batch")
    @Transactional
    @Test
    void shouldDeleteAllByIdInBatch() {
        LeadershipExperienceCalculation lc1 = persistenceService.save(getAll().get(0));
        LeadershipExperienceCalculation lc2 = persistenceService.save(getAll().get(1));

        List<Long> idsToDelete = List.of(lc1.getId(), lc2.getId());

        persistenceService.deleteAllById(idsToDelete);

        List<LeadershipExperienceCalculation> remaining = persistenceService.getAll();

        assertThat(remaining).isEmpty();
    }
}
