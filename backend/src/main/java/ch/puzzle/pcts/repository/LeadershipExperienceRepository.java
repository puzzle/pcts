package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadershipExperienceRepository extends SoftDeleteRepository<LeadershipExperience, Long> {
    List<LeadershipExperience> findAllByMember(Member member);

}
