package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends SoftDeleteRepository<Experience, Long> {
    @Query("SELECT e from Experience e where e.member = :member AND e.deletedAt IS NULL")
    List<Experience> findAllByMember(Member member);
}
