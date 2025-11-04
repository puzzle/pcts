package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleValidationServiceTest extends ValidationBaseServiceTest<Role, RoleValidationService> {

    @Mock
    RolePersistenceService persistenceService;

    @Spy
    @InjectMocks
    RoleValidationService validationService;

    @Override
    Role getModel() {
        return createRole("Role");
    }

    @Override
    RoleValidationService getService() {
        return validationService;
    }

    private static Role createRole(String name) {
        Role r = new Role();
        r.setName(name);
        return r;
    }

    static Stream<Arguments> invalidModelProvider() {
        String tooLongName = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments.of(createRole(null), "Role.name must not be null."),
                    Arguments.of(createRole(""), "Role.name must not be blank."),
                    Arguments.of(createRole("  "), "Role.name must not be blank."),
                    Arguments.of(createRole("S"), "Role.name size must be between 2 and 250, given S."),
                    Arguments.of(createRole("  S "), "Role.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createRole(tooLongName),
                                String.format("Role.name size must be between 2 and 250, given %s.", tooLongName)));

    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        Role role = getModel();

        when(persistenceService.getByName(role.getName())).thenReturn(Optional.of(new Role()));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(role));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        Role newRole = getModel();
        Role role = getModel();
        role.setId(2L);

        when(persistenceService.getByName(newRole.getName())).thenReturn(Optional.of(role));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, newRole));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Role role = getModel();

        doNothing().when((ValidationBase<Role>) validationService).validateOnCreate(any());

        validationService.validateOnCreate(role);

        verify(validationService).validateOnCreate(role);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Role role = getModel();

        doNothing().when((ValidationBase<Role>) validationService).validateOnUpdate(anyLong(), any());

        validationService.validateOnUpdate(id, role);

        verify(validationService).validateOnUpdate(id, role);
        verifyNoMoreInteractions(persistenceService);
    }
}
