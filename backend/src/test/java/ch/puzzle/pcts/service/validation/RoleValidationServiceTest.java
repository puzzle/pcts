package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RoleValidationServiceTest extends ValidationBaseServiceTest<Role, RoleValidationService> {

    @Mock
    private RolePersistenceService persistenceService;

    @InjectMocks
    private RoleValidationService validationService;

    @Override
    Role getModel() {
        return new Role(null, "Role", false);
    }

    @Override
    RoleValidationService getService() {
        return validationService;
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        Role role = getModel();

        when(persistenceService.getByName(role.getName())).thenReturn(Optional.of(new Role()));

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnCreate(role));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should Throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        Role newRole = getModel();
        Role role = getModel();
        role.setId(2L);

        when(persistenceService.getByName(newRole.getName())).thenReturn(Optional.of(role));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, newRole));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}
