package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleBusinessServiceTest {

    @Mock
    private RoleValidationService validationService;

    @Mock
    private RolePersistenceService persistenceService;

    @InjectMocks
    private RoleBusinessService businessService;

    @Captor
    ArgumentCaptor<Role> roleCaptor;

    @DisplayName("Should get role by id")
    @Test
    void shouldGetById() {
        Role role = new Role(1L, "Role1", false);
        when(persistenceService.getById(1L)).thenReturn(Optional.of(role));

        Role result = businessService.getById(1L);

        assertEquals(role, result);
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(1L));

        assertEquals("Role with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should get all roles")
    @Test
    void shouldGetAll() {
        List<Role> roles = List.of(new Role(1L, "Role1", false), new Role(2L, "Role2", false));
        when(persistenceService.getAll()).thenReturn(roles);

        List<Role> result = businessService.getAll();

        assertArrayEquals(roles.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(new ArrayList<>());

        List<Role> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @DisplayName("Should create role")
    @Test
    void shouldCreate() {
        Role role = new Role(1L, "Role1", false);
        when(persistenceService.create(role)).thenReturn(role);

        Role result = businessService.create(role);

        assertEquals(role, result);
        verify(validationService).validateOnCreate(role);
        verify(persistenceService).create(role);
    }

    @DisplayName("Should update role")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        Role role = new Role(1L, "Role1", false);
        when(persistenceService.update(id, role)).thenReturn(role);

        Role result = businessService.update(id, role);

        assertEquals(role, result);
        verify(validationService).validateOnUpdate(id, role);
        verify(persistenceService).update(id, role);
    }

    @DisplayName("Should delete role")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should trim role name")
    @Test
    void shouldTrimRoleName() {

        businessService.create(new Role(1L, " Role ", false));

        verify(persistenceService).create(roleCaptor.capture());
        Role savedRole = roleCaptor.getValue();

        assertEquals("Role", savedRole.getName());
        assertEquals(1L, savedRole.getId());
    }
}
