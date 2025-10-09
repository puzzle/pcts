package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <R>
 *            the repository of the entity
 * @param <S>
 *            the persistence service of the entity
 */
@SpringBootTest
@ActiveProfiles("test")
@Import(TestContainerConfiguration.class)
abstract class PersistenceBaseIT<T, R extends JpaRepository<T, Long>, S extends PersistenceBase<T, R>> {

    private final S service;

    @Autowired
    private PostgreSQLContainer<?> postgres;

    PersistenceBaseIT(S service) {
        this.service = service;
    }

    /**
     * Returns an object of the entity used to save it in the database
     */
    abstract T getCreateEntity();

    /**
     * Returns an object of the Entity used to update an object in the database
     */
    abstract T getUpdateEntity();

    /**
     * A list of all the objects with this datatype stored in the database shouldn't
     * contain soft deleted ones
     */
    abstract List<T> getAll();

    /**
     * A method to get the id of the entity
     */
    abstract Long getId(T entity);

    /**
     * A method to set the id of the entity
     */
    abstract void setId(T entity, Long id);

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should get entity by id")
    @Test
    @Order(1)
    void shouldGetEntityById() {
        Optional<T> entity = service.getById(2L);

        assertThat(entity).isPresent();
        assertThat(getId(entity.get())).isEqualTo(2L);
    }

    @DisplayName("Should get all entities")
    @Test
    @Transactional
    @Order(1)
    void shouldGetAllEntities() {
        List<T> all = service.getAll();

        assertThat(all).hasSize(getAll().size());
        assertThat(all).hasToString(getAll().toString());
    }

    @DisplayName("Should create entity")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        T entity = getCreateEntity();

        T result = service.save(entity);

        setId(entity, getId(result));
        assertThat(result).isEqualTo(entity);
    }

    @DisplayName("Should update entity")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 2;
        T entity = getUpdateEntity();
        setId(entity, id);
        service.save(entity);

        Optional<T> result = service.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get()).hasToString(entity.toString());
    }

    @DisplayName("Should delete entity")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        long id = 2;

        service.delete(id);

        Optional<T> result = service.getById(id);
        assertThat(result).isNotPresent();
    }
}
