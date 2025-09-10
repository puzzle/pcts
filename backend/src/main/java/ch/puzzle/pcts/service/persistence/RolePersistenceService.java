package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.RoleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolePersistenceService {
    private final RoleRepository repository;

    @Autowired
    public RolePersistenceService(RoleRepository repository) {
        this.repository = repository;
    }

    public Role create(Role role) {
        return repository.save(role);
    }

    public Role getById(long id) {
        return repository.findById(id).orElse(null);
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
