package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleValidationService {
    private final RolePersistenceService persistenceService;

    @Autowired
    public RoleValidationService(RolePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
