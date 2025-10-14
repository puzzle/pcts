package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.role.Role;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RolePersistenceServiceIT extends PersistenceBasicIT {
    @Autowired
    private RolePersistenceService persistenceService;

    @DisplayName("Should get role by id")
    @Test
    void shouldGetRoleById() {
        Optional<Role> role = persistenceService.getById(2L);

        assertThat(role).isPresent();
        assertThat(role.get().getId()).isEqualTo(2L);
    }

    @DisplayName("Should get all roles")
    @Test
    void shouldGetAllRoles() {
        List<Role> all = persistenceService.getAll();

        assertThat(all).hasSize(1);
        assertThat(all).extracting(Role::getName).containsExactlyInAnyOrder("Role 2");
    }

    @DisplayName("Should create role")
    @Transactional
    @Test
    void shouldCreate() {
        Role role = new Role(null, "Role 3", false);

        Role result = persistenceService.create(role);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo(role.getName());
        assertThat(result.getIsManagement()).isEqualTo(role.getIsManagement());
    }

    @DisplayName("Should update role")
    @Transactional
    @Test
    void shouldUpdate() {
        Long id = 2L;
        Role role = new Role(null, "Updated role", true);

        persistenceService.update(id, role);
        Optional<Role> result = persistenceService.getById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(role.getName()).isEqualTo("Updated role");
        assertThat(role.getIsManagement()).isTrue();
    }

    @DisplayName("Should delete role")
    @Transactional
    @Test
    void shouldDelete() {
        Long id = 2L;

        persistenceService.delete(id);

        Optional<Role> result = persistenceService.getById(id);
        assertThat(result).isNotPresent();
    }

    @DisplayName("Should get role by name")
    @Test
    void shouldGetRoleByName() {
        Optional<Role> result = persistenceService.getByName("Role 2");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Role 2");
        assertThat(result.get().getIsManagement()).isFalse();
    }
}
