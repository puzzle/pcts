package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import org.springframework.stereotype.Service;

@Service
public class RoleBusinessService {
    private final RoleValidationService validationService;
    private final RolePersistenceService persistenceService;

    public RoleBusinessService(RoleValidationService validationService, RolePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }
}
