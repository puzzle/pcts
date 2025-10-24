package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.Model;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <R>
 *            the repository of the entity
 * @param <S>
 *            the persistence service of the entity
 */
abstract class PersistenceBaseIT<T extends Model, R extends JpaRepository<T, Long>, S extends PersistenceBase<T, R>>
        extends
            PersistenceCoreIT {

    private final S service;

    PersistenceBaseIT(S service) {
        this.service = service;
    }

    /**
     * Returns an object of the entity used to save it in the database
     */
    abstract T getModel();
    /**
     * A list of all the objects with this datatype stored in the database shouldn't
     * contain soft deleted ones
     */
    abstract List<T> getAll();

    @DisplayName("Should get entity by id")
    @Test
    void shouldGetEntityById() {
        Optional<T> entity = service.getById(2L);

        assertThat(entity).isPresent();
        assertThat(entity.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all entities")
    @Test
    @Transactional
    void shouldGetAllEntities() {
        List<T> all = service.getAll();

        assertThat(all).hasSize(getAll().size());
        assertEquals(all, getAll());
    }

    @DisplayName("Should create entity")
    @Transactional
    @Test
    void shouldCreate() {
        T entity = getModel();

        T result = service.save(entity);

        entity.setId(result.getId());
        assertThat(result).isEqualTo(entity);
    }

    @DisplayName("Should update entity")
    @Transactional
    @Test
    void shouldUpdate() {
        Long id = 2L;
        T entity = this.getAll().getFirst();
        entity.setId(id);
        service.save(entity);

        Optional<T> result = service.getById(id);

        assertThat(result).isPresent();
        assertEquals(result.get(), entity);
    }

    @DisplayName("Should delete entity")
    @Transactional
    @Test
    void shouldDelete() {
        Long id = 2L;

        service.delete(id);

        Optional<T> result = service.getById(id);
        assertThat(result).isNotPresent();
    }
}
