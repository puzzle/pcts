package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadershipExperienceCalculationRepository
        extends
            CalculationChildRepository<LeadershipExperienceCalculation, Long> {
    List<LeadershipExperienceCalculation> findByLeadershipExperienceId(Long leadershipExperienceId);
}
