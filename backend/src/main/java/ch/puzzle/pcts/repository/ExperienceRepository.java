package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceRepository extends SoftDeleteRepository<Experience, Long> {
    List<Experience> findAllByMember(Member member);
}
