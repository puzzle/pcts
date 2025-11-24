package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import java.util.List;
import java.util.Map;
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
    Role getValidModel() {
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
                .of(Arguments.of(createRole(null), List.of(Map.of(FieldKey.CLASS, "Role", FieldKey.FIELD, "name"))),
                    Arguments.of(createRole(""), List.of(Map.of(FieldKey.CLASS, "Role", FieldKey.FIELD, "name"))),
                    Arguments.of(createRole("  "), List.of(Map.of(FieldKey.CLASS, "Role", FieldKey.FIELD, "name"))),
                    Arguments
                            .of(createRole("S"),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Role",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createRole("  S "),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Role",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    "S"))),
                    Arguments
                            .of(createRole(tooLongName),
                                List
                                        .of(Map
                                                .of(FieldKey.CLASS,
                                                    "Role",
                                                    FieldKey.FIELD,
                                                    "name",
                                                    FieldKey.MIN,
                                                    "2",
                                                    FieldKey.MAX,
                                                    "250",
                                                    FieldKey.IS,
                                                    tooLongName))));

    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        Role role = getValidModel();

        when(persistenceService.getByName(role.getName())).thenReturn(Optional.of(new Role()));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(role));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "Role", FieldKey.ENTITY, ROLE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        Role newRole = getValidModel();
        Role role = getValidModel();
        role.setId(2L);

        when(persistenceService.getByName(newRole.getName())).thenReturn(Optional.of(role));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, newRole));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_UNIQUE), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "name", FieldKey.IS, "Role", FieldKey.ENTITY, ROLE)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        Role role = getValidModel();

        doNothing().when((ValidationBase<Role>) validationService).validateOnCreate(any());

        validationService.validateOnCreate(role);

        verify(validationService).validateOnCreate(role);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        Role role = getValidModel();

        doNothing().when((ValidationBase<Role>) validationService).validateOnUpdate(anyLong(), any());

        validationService.validateOnUpdate(id, role);

        verify(validationService).validateOnUpdate(id, role);
        verifyNoMoreInteractions(persistenceService);
    }
}
