package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.degree.Degree;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeRepository extends SoftDeleteRepository<Degree, Long> {
}
