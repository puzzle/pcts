package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import java.util.Objects;
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

    public void validateOnSetData(Role role) {
        if (role.getId() != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.ID_IS_NOT_NULL);
        }

        validateName(role.getName());
    }

    public void validateName(String name) {
        if (name == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be null", ErrorKey.ROLE_NAME_IS_NULL);
        }

        if (Objects.equals(name, "")) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be empty", ErrorKey.ROLE_NAME_IS_EMPTY);
        }
    }

    public void validateIfExists(long id) {
        if (persistenceService.getById(id) == null) {
            throw new PCTSException(HttpStatus.NOT_FOUND, "Role does not exists", ErrorKey.NOT_FOUND);
        }
    }
}
