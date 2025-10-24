package ch.puzzle.pcts.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <I>
 *            the Type of the ID of the entity
 **/
@NoRepositoryBean
public interface SoftDeleteRepository<T, I> extends JpaRepository<T, I> {
    @Query("SELECT m FROM #{#entityName} m WHERE m.deletedAt IS NULL AND m.id = :id")
    Optional<T> findById(Long id);

    @Query("SELECT m FROM #{#entityName} m WHERE m.deletedAt IS NULL")
    List<T> findAll();
}
