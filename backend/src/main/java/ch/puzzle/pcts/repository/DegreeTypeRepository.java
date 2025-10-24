package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeTypeRepository extends JpaRepository<DegreeType, Long> {
    @Query(value = "SELECT * FROM degree_type WHERE deleted_at IS NULL AND id = :id", nativeQuery = true)
    Optional<DegreeType> findById(Long id);

    @Query(value = "SELECT * FROM degree_type WHERE deleted_at IS NULL", nativeQuery = true)
    List<DegreeType> findAll();
}
