package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.role.Role;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class RolePersistenceServiceIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private RolePersistenceService persistenceService;

    @DisplayName("Should establish DB connection")
    @Test
    @Order(0)
    void shouldEstablishConnection() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @DisplayName("Should get role by id")
    @Test
    @Order(1)
    void shouldGetRoleById() {
        Optional<Role> role = persistenceService.getById(2L);

        assertThat(role.isPresent()).isTrue();
        assertThat(role.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all roles")
    @Test
    @Order(1)
    void shouldGetAllRoles() {
        List<Role> all = persistenceService.getAll();

        assertThat(all).hasSize(1);
        assertThat(all).extracting(Role::getName).containsExactlyInAnyOrder("Role 2");
    }

    @DisplayName("Should create role")
    @Transactional
    @Test
    @Order(2)
    void shouldCreate() {
        Role role = new Role(null, "Role 3", false);

        persistenceService.create(role);
        Optional<Role> result = persistenceService.getById(3L);

        assertThat(result.isPresent()).isTrue();
        assertThat(role.getId()).isEqualTo(3L);
    }

    @DisplayName("Should update role")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 2;
        Role role = new Role(null, "Updated role", true);

        persistenceService.update(id, role);
        Optional<Role> result = persistenceService.getById(id);

        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(role.getName()).isEqualTo("Updated role");
        assertThat(role.getIsManagement()).isEqualTo(true);
    }

    @DisplayName("Should delete role")
    @Transactional
    @Test
    @Order(3)
    void shouldDelete() {
        long id = 2;

        persistenceService.delete(id);
        Optional<Role> result = persistenceService.getById(id);

        assertThat(result.isPresent()).isFalse();
    }
}
