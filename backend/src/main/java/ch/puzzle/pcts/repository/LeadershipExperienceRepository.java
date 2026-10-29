package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadershipExperienceRepository extends SoftDeleteRepository<LeadershipExperience, Long> {
    @Query("SELECT l from LeadershipExperience l where l.member = :member AND l.deletedAt IS NULL")
    List<LeadershipExperience> findAllByMember(Member member);

}
