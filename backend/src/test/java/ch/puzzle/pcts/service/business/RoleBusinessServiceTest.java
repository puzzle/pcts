package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.Collections;
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

    @Mock
    private Role role;

    @Mock
    private List<Role> roles;

    @InjectMocks
    private RoleBusinessService businessService;

    @Captor
    ArgumentCaptor<Role> roleCaptor;

    @DisplayName("Should get role by id")
    @Test
    void shouldGetById() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(role));

        Role result = businessService.getById(id);

        assertEquals(role, result);
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(id));

        assertEquals(String.format("Role with id: %d does not exist.", id), exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }

    @DisplayName("Should get all roles")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(roles);
        when(roles.size()).thenReturn(2);

        List<Role> result = businessService.getAll();

        assertEquals(roles, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<Role> result = businessService.getAll();

        assertEquals(0, result.size());
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should create role")
    @Test
    void shouldCreate() {
        when(persistenceService.save(role)).thenReturn(role);

        Role result = businessService.create(role);

        assertEquals(role, result);
        verify(validationService).validateOnCreate(role);
        verify(persistenceService).save(role);
    }

    @DisplayName("Should update role")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(role)).thenReturn(role);

        Role result = businessService.update(id, role);

        assertEquals(role, result);
        verify(validationService).validateOnUpdate(id, role);
        verify(role).setId(id);
        verify(persistenceService).save(role);
    }

    @DisplayName("Should delete role")
    @Test
    void shouldDelete() {
        Long id = 1L;

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

}
