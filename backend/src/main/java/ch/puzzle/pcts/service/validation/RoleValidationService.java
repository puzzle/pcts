package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import java.util.Map;
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
        if (UniqueNameValidationUtil.nameAlreadyUsed(role.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    ErrorKey.ATTRIBUTE_UNIQUE,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                "role",
                                                FieldKey.FIELD,
                                                "name",
                                                FieldKey.IS,
                                                role.getName()));

        }
    }

    @Override
    public void validateOnUpdate(Long id, Role role) {
        super.validateOnUpdate(id, role);
        if (UniqueNameValidationUtil.nameExcludingIdAlreadyUsed(id, role.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    ErrorKey.ATTRIBUTE_UNIQUE,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                "role",
                                                FieldKey.FIELD,
                                                "name",
                                                FieldKey.IS,
                                                role.getName()));

        }
    }
}
