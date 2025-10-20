package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RolePersistenceServiceIT extends PersistenceBaseIT<Role, RoleRepository, RolePersistenceService> {

    private final RolePersistenceService service;

    @Autowired
    RolePersistenceServiceIT(RolePersistenceService service) {
        super(service);
        this.service = service;
    }

    @Override
    Role getModel() {
        return new Role(null, "Role 3", false);
    }

    List<Role> getAll() {
        return List.of(new Role(2L, "Role 2", false));
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
