package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoleBusinessService extends BusinessBase<Role> {

    public RoleBusinessService(RoleValidationService validationService, RolePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<Role> getAll() {
        return persistenceService.getAll();
    }
}
