package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.ROLES;
import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
    Role getModel() {
        return Role.Builder.builder().withName("Role 3").withIsManagement(false).build();
    }

    List<Role> getAll() {
        return ROLES;
    }

    @DisplayName("Should get role by name")
    @Test
    void shouldGetRoleByName() {
        Optional<Role> result = service.getByName("Role 2");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Role 2");
        assertThat(result.get().getIsManagement()).isFalse();
    }
}
