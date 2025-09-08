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
        return repository.getByID(id);
    }

    public List<Role> getAll() {
        return repository.findAll();
    }

    public Role update(Long id, Role role) {
        repository.update(id, role.getName(), role.getIsManagement());
        return repository.getByID(id);
    }

    public void delete(Long id) {
        repository.softDeleteById(id);
    }
}
