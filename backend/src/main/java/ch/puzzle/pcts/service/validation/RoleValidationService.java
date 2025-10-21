package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleValidationService extends ValidationBase<Role> {
    private final RolePersistenceService persistenceService;

    public RoleValidationService(RolePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(Role role) {
        super.validateOnCreate(role);
        validateNameUniqueness(role.getName());
    }

    @Override
    public void validateOnUpdate(Long id, Role role) {
        super.validateOnUpdate(id, role);
        validateNameUniqueExcludingSelf(id, role.getName());
    }

    private void validateNameUniqueness(String name) {
        persistenceService.getByName(name).ifPresent(role -> {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.ROLE_NAME_ALREADY_EXISTS);
        });
    }

    private void validateNameUniqueExcludingSelf(Long id, String name) {
        Optional<Role> existingRole = persistenceService.getByName(name);
        existingRole.ifPresent(role -> {
            if (!role.getId().equals(id)) {
                throw new PCTSException(HttpStatus.BAD_REQUEST,
                                        "Name already exists",
                                        ErrorKey.ROLE_NAME_ALREADY_EXISTS);
            }
        });
    }
}
