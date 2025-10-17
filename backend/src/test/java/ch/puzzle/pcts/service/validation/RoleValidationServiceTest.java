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
    void validate() {

    }

    @Override
    RoleValidationService getService() {
        return validationService;
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        Role role = new Role();
        role.setName("Existing Role");

        when(persistenceService.getByName("Existing Role")).thenReturn(Optional.of(new Role()));

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnCreate(role));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.ROLE_NAME_ALREADY_EXISTS, exception.getErrorKey());
    }

    @DisplayName("Should Throw Exception on validateOnUpdate() when name already exists for another Role")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExistsForAnotherRole() {
        long id = 1L;
        String name = "Role";

        Role role = new Role();
        role.setName(name);

        Role anotherRole = new Role();
        anotherRole.setName(name);
        anotherRole.setId(2L);

        when(persistenceService.getById(id)).thenReturn(Optional.of(role));
        when(persistenceService.getByName(name)).thenReturn(Optional.of(anotherRole));

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnUpdate(id, role));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.ROLE_NAME_ALREADY_EXISTS, exception.getErrorKey());
    }

    @DisplayName("Should not Throw Exception on validateOnUpdate() when name stays the same")
    @Test
    void shouldNotThrowExceptionOnValidateOnUpdateWhenNameStaysTheSame() {
        long id = 1L;
        String name = "Role";

        Role newRole = new Role();
        newRole.setName(name);

        Role oldRole = new Role();
        oldRole.setName(name);
        oldRole.setId(id);

        when(persistenceService.getById(id)).thenReturn(Optional.of(newRole));
        when(persistenceService.getByName(name)).thenReturn(Optional.of(oldRole));

        assertDoesNotThrow(() -> validationService.validateOnUpdate(id, newRole));

    }
}
