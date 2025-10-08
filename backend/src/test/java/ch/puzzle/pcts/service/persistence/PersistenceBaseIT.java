package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <S>
 *            the persistence service of the entity
 */
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public abstract class PersistenceBaseIT<T, S extends PersistenceBase<T>> {

    private final S service;

    PersistenceBaseIT(S service) {
        this.service = service;
    }

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    abstract T getCreateEntity();
    abstract T getUpdateEntity();
    abstract Long getId(T entity);
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
    @Order(1)
    void shouldGetAllEntities() {
        List<T> all = service.getAll();

        assertThat(all).hasSize(1);
    }

    @DisplayName("Should create entity")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        T entity = getCreateEntity();

        T result = service.create(entity);

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

        service.update(id, entity);
        Optional<T> result = service.getById(id);

        setId(entity, 2L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(entity);

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
