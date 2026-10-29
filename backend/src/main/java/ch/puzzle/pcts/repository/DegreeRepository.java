package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends SoftDeleteRepository<Degree, Long> {
    @Query("SELECT d from Degree d where d.member = :member AND d.deletedAt IS NULL")
    List<Degree> findAllByMember(Member member);
}
