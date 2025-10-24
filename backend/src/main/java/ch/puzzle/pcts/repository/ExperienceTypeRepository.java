package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceTypeRepository extends JpaRepository<ExperienceType, Long> {
    @Query(value = "SELECT * FROM experience_type WHERE deleted_at IS NULL AND id = :id", nativeQuery = true)
    Optional<ExperienceType> findById(Long id);

    @Query(value = "SELECT * FROM experience_type WHERE deleted_at IS NULL", nativeQuery = true)
    List<ExperienceType> findAll();
}
