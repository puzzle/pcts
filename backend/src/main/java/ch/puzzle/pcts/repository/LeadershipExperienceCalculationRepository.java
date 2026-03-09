package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import java.util.List;

public interface LeadershipExperienceCalculationRepository
        extends
            CalculationChildRepository<LeadershipExperienceCalculation, Long> {
    List<LeadershipExperienceCalculation> findByLeadershipExperienceId(Long leadershipExperienceId);
}
