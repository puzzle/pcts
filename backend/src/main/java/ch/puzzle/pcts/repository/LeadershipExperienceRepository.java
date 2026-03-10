package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadershipExperienceRepository extends SoftDeleteRepository<LeadershipExperience, Long> {

}
