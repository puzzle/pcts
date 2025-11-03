package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.Model;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    private final TimeZone originalTimeZone = TimeZone.getDefault();

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

    /**
     * Setting the timezone is necessary because Timestamp is somehow affected by
     * the timezone of the system the JVM runs on. This should be fixed in another
     * ticket by using a newer API than Timestamp()
     */
    @BeforeEach
    void setUtcTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterEach
    void restoreDefaultTimeZone() {
        TimeZone.setDefault(originalTimeZone);
    }

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
        assertEquals(getAll(), all);
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
        T entity = getModel();
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
