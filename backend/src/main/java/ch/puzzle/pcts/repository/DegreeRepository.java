package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends SoftDeleteRepository<Degree, Long> {
    List<Degree> findAllByMember(Member member);
}
