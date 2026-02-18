package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.GENERIC_2_ID;
import static ch.puzzle.pcts.util.TestData.INVALID_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <R>
 *            the repository of the entity
 * @param <S>
 *            the persistence service of the entity
 */
@WithMockUser(username = "PersistenceIT User")
abstract class PersistenceBaseIT<T extends Model, R extends JpaRepository<T, Long>, S extends PersistenceBase<T, R>>
        extends
            PersistenceCoreIT {

    protected final S persistenceService;

    PersistenceBaseIT(S persistenceService) {
        this.persistenceService = persistenceService;
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
        T entity = persistenceService.getById(GENERIC_2_ID);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(GENERIC_2_ID);
    }

    @DisplayName("Should throw exception when id is not found")
    @Test
    void shouldThrowExceptionWhenIdIsNotFound() {
        Map<FieldKey, String> expectedAttributes = Map
                .of(FieldKey.FIELD,
                    "id",
                    FieldKey.IS,
                    String.valueOf(INVALID_ID),
                    FieldKey.ENTITY,
                    persistenceService.entityName());

        PCTSException exception = assertThrows(PCTSException.class, () -> persistenceService.getById(INVALID_ID));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List.of(expectedAttributes), exception.getErrorAttributes());
    }

    @DisplayName("Should get all entities")
    @Test
    @Transactional
    void shouldGetAllEntities() {
        List<T> all = persistenceService.getAll();
        assertThat(all).hasSize(getAll().size());
        assertEquals(getAll(), all);
    }

    @DisplayName("Should create entity")
    @Transactional
    @Test
    void shouldCreate() {
        T entity = getModel();

        T result = persistenceService.save(entity);

        entity.setId(result.getId());
        assertThat(result).isEqualTo(entity);
    }

    @DisplayName("Should update entity")
    @Transactional
    @Test
    void shouldUpdate() {
        T entity = getModel();
        entity.setId(GENERIC_2_ID);
        persistenceService.save(entity);

        T result = persistenceService.getById(GENERIC_2_ID);

        assertThat(result).isNotNull();
        assertEquals(entity, result);
    }

    @DisplayName("Should delete entity")
    /*
     * We avoid using @Transactional here because it causes Hibernate to return
     * Optional.empty when calling getById, without actually hitting the database.
     * To ensure isolation between tests, we use @DirtiesContext instead. This
     * forces the Spring container to reset after this test class, so the tests do
     * not depend on execution order.
     */
    @DirtiesContext
    @Test
    void shouldDelete() {
        persistenceService.delete(GENERIC_2_ID);

        assertThrows(PCTSException.class, () -> persistenceService.getById(GENERIC_2_ID));
    }
}
