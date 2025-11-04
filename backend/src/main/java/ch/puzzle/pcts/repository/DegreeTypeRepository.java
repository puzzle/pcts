package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeTypeRepository extends SoftDeleteRepository<DegreeType, Long> {
    Optional<DegreeType> findByName(String name);
}
