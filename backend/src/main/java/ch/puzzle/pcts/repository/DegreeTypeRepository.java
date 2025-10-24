package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeTypeRepository extends SoftDeleteRepository<DegreeType>, JpaRepository<DegreeType, Long> {
}
