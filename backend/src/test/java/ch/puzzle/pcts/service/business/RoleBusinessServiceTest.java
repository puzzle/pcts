package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RoleBusinessServiceTest {

    @Mock
    private RoleValidationService validationService;

    @Mock
    private RolePersistenceService persistenceService;

    @InjectMocks
    private RoleBusinessService businessService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should get role by id")
    @Test
    void shouldGetById() {
        Role role = new Role(1L, "Role1", false);
        when(persistenceService.getById(1L)).thenReturn(Optional.of(role));

        Role result = businessService.getById(1L);

        assertEquals(role, result);
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
        Role role = new Role(id, "Role1", false);
        when(persistenceService.getById(id)).thenReturn(Optional.of(role));

        Role result = businessService.delete(id);

        assertEquals(role, result);
        verify(validationService).validateOnDelete(id);
        verify(persistenceService).getById(id);
        verify(persistenceService).delete(id);
    }
}
