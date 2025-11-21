package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.OrganisationUnitValidationService;
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
class OrganisationUnitBusinessServiceTest {

    @Mock
    private OrganisationUnitValidationService validationService;

    @Mock
    private OrganisationUnitPersistenceService persistenceService;

    @Mock
    private OrganisationUnit organisationUnit;

    @Mock
    private List<OrganisationUnit> organisationUnits;

    @InjectMocks
    private OrganisationUnitBusinessService businessService;

    @DisplayName("Should get organisationUnit by id")
    @Test
    void shouldGetById() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(organisationUnit));

        OrganisationUnit result = businessService.getById(id);

        assertEquals(organisationUnit, result);
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
        assertEquals(List
                .of(Map.of(FieldKey.FIELD, "id", FieldKey.IS, id.toString(), FieldKey.ENTITY, "organisationUnit")),
                     exception.getErrorAttributes());
        verify(persistenceService).getById(id);
        verify(validationService).validateOnGetById(id);
    }

    @DisplayName("Should get all organisationUnits")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(organisationUnits);
        when(organisationUnits.size()).thenReturn(2);

        List<OrganisationUnit> result = businessService.getAll();

        assertEquals(organisationUnits, result);
        assertEquals(2, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<OrganisationUnit> result = businessService.getAll();

        assertEquals(0, result.size());
        verify(persistenceService).getAll();
        verifyNoInteractions(validationService);
    }

    @DisplayName("Should create organisationUnit")
    @Test
    void shouldCreate() {
        when(persistenceService.save(organisationUnit)).thenReturn(organisationUnit);

        OrganisationUnit result = businessService.create(organisationUnit);

        assertEquals(organisationUnit, result);
        verify(validationService).validateOnCreate(organisationUnit);
        verify(persistenceService).save(organisationUnit);
    }

    @DisplayName("Should update organisationUnit")
    @Test
    void shouldUpdate() {
        Long id = 1L;
        when(persistenceService.save(organisationUnit)).thenReturn(organisationUnit);
        when(persistenceService.getById(id)).thenReturn(Optional.of(organisationUnit));

        OrganisationUnit result = businessService.update(id, organisationUnit);

        assertEquals(organisationUnit, result);
        verify(validationService).validateOnUpdate(id, organisationUnit);
        verify(organisationUnit).setId(id);
        verify(persistenceService).save(organisationUnit);
    }

    @DisplayName("Should throw exception when updating non-existing organisation unit")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        Long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.update(id, organisationUnit));

        verify(persistenceService).getById(id);
        verify(validationService, never()).validateOnUpdate(any(), any());
        verify(persistenceService, never()).save(any());
    }

    @DisplayName("Should delete organisationUnit")
    @Test
    void shouldDelete() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(organisationUnit));

        businessService.delete(id);

        verify(validationService).validateOnDelete(id);
        verify(persistenceService).delete(id);
    }

    @DisplayName("Should throw exception when deleting non-existing organisation unit")
    @Test
    void shouldThrowExceptionWhenNotFound() {
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        assertThrows(PCTSException.class, () -> businessService.delete(id));

        verify(persistenceService).getById(id);
        verify(persistenceService, never()).delete(id);
    }
}
