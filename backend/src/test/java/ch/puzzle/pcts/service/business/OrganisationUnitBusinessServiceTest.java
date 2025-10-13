package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.OrganisationUnitValidationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganisationUnitBusinessServiceTest {

    @Mock
    private OrganisationUnitValidationService validationService;

    @Mock
    private OrganisationUnitPersistenceService persistenceService;

    @InjectMocks
    private OrganisationUnitBusinessService businessService;

    @Captor
    ArgumentCaptor<OrganisationUnit> organisationUnitCaptor;

    @DisplayName("Should get organisationUnit by id")
    @Test
    void shouldGetById() {
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "OrganisationUnit1");
        when(persistenceService.getById(1L)).thenReturn(Optional.of(organisationUnit));

        OrganisationUnit result = businessService.getById(1L);

        assertEquals(organisationUnit, result);
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should throw exception")
    @Test
    void shouldThrowException() {
        when(persistenceService.getById(1L)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(1L));

        assertEquals("Organisation unit with id: " + 1 + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
        verify(persistenceService).getById(1L);
    }

    @DisplayName("Should get all organisationUnits")
    @Test
    void shouldGetAll() {
        List<OrganisationUnit> organisationUnits = List
                .of(new OrganisationUnit(1L, "OrganisationUnit1"), new OrganisationUnit(2L, "OrganisationUnit2"));
        when(persistenceService.getAll()).thenReturn(organisationUnits);

        List<OrganisationUnit> result = businessService.getAll();

        assertArrayEquals(organisationUnits.toArray(), result.toArray());
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(new ArrayList<>());

        List<OrganisationUnit> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @DisplayName("Should create organisationUnit")
    @Test
    void shouldCreate() {
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "OrganisationUnit1");
        when(persistenceService.create(organisationUnit)).thenReturn(organisationUnit);

        OrganisationUnit result = businessService.create(organisationUnit);

        assertEquals(organisationUnit, result);
        verify(validationService).validateOnCreate(organisationUnit);
        verify(persistenceService).create(organisationUnit);
    }

    @DisplayName("Should update organisationUnit")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "OrganisationUnit1");
        when(persistenceService.update(id, organisationUnit)).thenReturn(organisationUnit);

        OrganisationUnit result = businessService.update(id, organisationUnit);

        assertEquals(organisationUnit, result);
        verify(validationService).validateOnUpdate(id, organisationUnit);
        verify(persistenceService).update(id, organisationUnit);
    }

    @DisplayName("Should delete organisationUnit")
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

        businessService.create(new OrganisationUnit(1L, " OrganisationUnit "));

        verify(persistenceService).create(organisationUnitCaptor.capture());
        OrganisationUnit savedOrganisationUnit = organisationUnitCaptor.getValue();

        assertEquals("OrganisationUnit", savedOrganisationUnit.getName());
        assertEquals(1L, savedOrganisationUnit.getId());
    }
}
