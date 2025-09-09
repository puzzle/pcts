package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoleBusinessService {
    private final RoleValidationService validationService;
    private final RolePersistenceService persistenceService;

    public RoleBusinessService(RoleValidationService validationService, RolePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public List<Role> getAll() {
        return persistenceService.getAll();
    }

    public Role getById(long id) {
        validationService.validateOnGetById(id);
        return persistenceService.getById(id);
    }

    public Role create(Role role) {
        validationService.validateOnCreate(role);
        return persistenceService.create(role);
    }

    public Role update(Long id, Role role) {
        validationService.validateOnUpdate(id, role);
        return persistenceService.update(id, role);
    }

    public Role delete(Long id) {
        validationService.validateOnDelete(id);
        Role deletedRole = persistenceService.getById(id);
        persistenceService.delete(id);
        return deletedRole;
    }
}
