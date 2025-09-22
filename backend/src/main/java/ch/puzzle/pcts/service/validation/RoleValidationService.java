package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RoleValidationService {
    private final RolePersistenceService persistenceService;

    @Autowired
    public RoleValidationService(RolePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnCreate(Role role) {
        validateIfIdIsNull(role.getId());
        validateName(role.getName());
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, Role role) {
        validateIfExists(id);
        validateIfIdIsNull(role.getId());
        validateName(role.getName());
    }

    private void validateIfIdIsNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.ID_IS_NOT_NULL);
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be null", ErrorKey.ROLE_NAME_IS_NULL);
        }

        if (name.isBlank()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be empty", ErrorKey.ROLE_NAME_IS_EMPTY);
        }

        if (persistenceService.getByName(name) != null) {
            throw new PCTSException(HttpStatus.CONFLICT, "Name already exists", ErrorKey.ROLE_NAME_ALREADY_EXISTS);
        }
    }

    private void validateIfExists(long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Role with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }
}
