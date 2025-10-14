package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RolePersistenceService {
    private final RoleRepository repository;

    public RolePersistenceService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role create(Role role) {
        return repository.save(role);
    }

    public Optional<Role> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Role> getByName(String name) {
        return repository.findByName(name);
    }

    public List<Role> getAll() {
        return repository.findAll();
    }

    public Role update(Long id, Role role) {
        role.setId(id);
        return repository.save(role);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
