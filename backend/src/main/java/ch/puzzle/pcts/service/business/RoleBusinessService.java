package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.ROLE;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.RoleRepository;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoleBusinessService
        extends
            BusinessBase<Role, RoleRepository, RoleValidationService, RolePersistenceService> {

    public RoleBusinessService(RoleValidationService validationService, RolePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<Role> getAll() {
        return persistenceService.getAll();
    }

    @Override
    protected String entityName() {
        return ROLE;
    }
}
