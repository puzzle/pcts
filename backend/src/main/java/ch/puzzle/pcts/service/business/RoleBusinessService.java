package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
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

    public Role getById(Long id) {
        validationService.validateOnGet(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Role with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public Role create(Role role) {
        validationService.validateOnCreate(role);
        return persistenceService.save(role);
    }

    public Role update(Long id, Role role) {
        validationService.validateOnUpdate(id, role);
        role.setId(id);
        return persistenceService.save(role);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
