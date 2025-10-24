package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeTypeRepository extends SoftDeleteRepository<DegreeType, Long> {
}
