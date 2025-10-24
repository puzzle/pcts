package ch.puzzle.pcts.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @param <T>
 *            the Type or entity of the repository
 **/
@Repository
public interface SoftDeleteRepository<T> {
    @Query("SELECT m FROM #{#entityName} m WHERE m.deletedAt IS NULL AND m.id = :id")
    Optional<T> findById(Long id);

    @Query("SELECT m FROM #{#entityName} m WHERE m.deletedAt IS NULL")
    List<T> findAll();
}
