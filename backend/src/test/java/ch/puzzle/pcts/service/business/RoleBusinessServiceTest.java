package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.RolePersistenceService;
import ch.puzzle.pcts.service.validation.RoleValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleBusinessServiceTest
        extends
            BaseBusinessTest<Role, RolePersistenceService, RoleValidationService, RoleBusinessService> {

    @Mock
    private Role role;

    @Mock
    private List<Role> roles;

    @Mock
    private RolePersistenceService persistenceService;

    @Mock
    private RoleValidationService validationService;

    @InjectMocks
    private RoleBusinessService businessService;

    @Override
    Role getModel() {
        return role;
    }

    @Override
    RolePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    RoleValidationService getValidationService() {
        return validationService;
    }

    @Override
    RoleBusinessService getBusinessService() {
        return businessService;
    }

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(roles);
        when(roles.size()).thenReturn(2);

        List<Role> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(roles, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<Role> result = businessService.getAll();

        assertEquals(0, result.size());
    }
}
