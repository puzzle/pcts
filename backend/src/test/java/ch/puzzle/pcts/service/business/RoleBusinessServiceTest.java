package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.ROLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertIterableEquals(List.of(Map.of(FieldKey.FIELD, "id", FieldKey.IS, id.toString(), FieldKey.ENTITY, ROLE)),
                             exception.getErrorAttributes());
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
        when(persistenceService.getById(id)).thenReturn(Optional.of(role));

        Role result = businessService.update(id, role);

        assertEquals(role, result);
        verify(validationService).validateOnUpdate(id, role);
        verify(role).setId(id);
        verify(persistenceService).save(role);
    }

    @DisplayName("Should throw exception when updating non-existing role")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        Long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(id, role));

        verify(persistenceService).getById(id);
        verify(validationService, never()).validateOnUpdate(any(), any());
        verify(persistenceService, never()).save(any());
    }

    @DisplayName("Should delete role")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(role));

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should throw exception when deleting non-existing role")
    @Test
    void shouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(id));

        verify(persistenceService).getById(id);
        verify(persistenceService, never()).delete(id);
    }

}
