package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.ROLE;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.RoleRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RolePersistenceService extends PersistenceBase<Role, RoleRepository> {
    private final RoleRepository roleRepository;

    public RolePersistenceService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    @Override
    public String entityName() {
        return ROLE;
    }

    public Optional<Role> getByName(String name) {
        return roleRepository.findByName(name);
    }
}
