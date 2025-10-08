package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.RoleRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RolePersistenceService extends PersistenceBase<Role, RoleRepository> {
    private final RoleRepository repository;

    public RolePersistenceService(RoleRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Optional<Role> getByName(String name) {
        return repository.findByName(name);
    }
}
