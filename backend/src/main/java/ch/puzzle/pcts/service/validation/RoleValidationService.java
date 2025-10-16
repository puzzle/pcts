package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleValidationService {
    private final RolePersistenceService persistenceService;

    public RoleValidationService(RolePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnCreate(Role role) {
        validateIfIdIsNull(role.getId());
        validateNameConstraints(role.getName(), role.getId());
        validateNameUniqueness(role.getName());
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, Role role) {
        validateIfExists(id);
        validateIfIdIsNull(role.getId());
        validateNameConstraints(role.getName(), role.getId());
        validateNameUniqueExcludingSelf(id, role.getName());
    }

    private void validateIfIdIsNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.ID_IS_NOT_NULL);
        }
    }

    private void validateNameConstraints(String name, Long id) {
        if (name == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be null", ErrorKey.ROLE_NAME_IS_NULL);
        }

        if (name.isBlank()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be empty", ErrorKey.ROLE_NAME_IS_EMPTY);
        }

        Optional<Role> sameNameRole = persistenceService
                .getByName(name)
                .filter(role -> role.getId() != null && role.getId().equals(id));

        if (sameNameRole.isPresent()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.ROLE_NAME_ALREADY_EXISTS);
        }
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

    private void validateIfExists(Long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Role with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }
}
