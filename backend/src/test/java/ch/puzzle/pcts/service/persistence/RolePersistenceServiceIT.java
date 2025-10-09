package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.RoleRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RolePersistenceServiceIT extends PersistenceBaseIT<Role, RoleRepository, RolePersistenceService> {

    private final RolePersistenceService service;

    @Autowired
    RolePersistenceServiceIT(RolePersistenceService service) {
        super(service);
        this.service = service;
    }

    @Override
    Role getCreateEntity() {
        return new Role(null, "Role 3", false);
    }

    @Override
    Role getUpdateEntity() {
        return new Role(null, "Updated role", true);
    }

    @Override
    Long getId(Role role) {
        return role.getId();
    }

    @Override
    void setId(Role role, Long id) {
        role.setId(id);
    }

    @DisplayName("Should get role by name")
    @Test
    @Order(1)
    void shouldGetRoleByName() {
        Optional<Role> result = service.getByName("Role 2");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Role 2");
        assertThat(result.get().getIsManagement()).isFalse();
    }
}
