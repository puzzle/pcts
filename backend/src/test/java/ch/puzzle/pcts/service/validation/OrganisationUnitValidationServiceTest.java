package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganisationUnitValidationServiceTest
        extends
            ValidationBaseServiceTest<OrganisationUnit, OrganisationUnitValidationService> {

    @InjectMocks
    private OrganisationUnitValidationService service;

    @Mock
    private OrganisationUnitPersistenceService persistenceService;

    @Override
    OrganisationUnit getModel() {
        return new OrganisationUnit(null, "Organisation Unit");
    }

    @Override
    OrganisationUnitValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        OrganisationUnit organisationUnit = getModel();

        when(persistenceService.getByName(organisationUnit.getName())).thenReturn(Optional.of(new OrganisationUnit()));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(organisationUnit));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        OrganisationUnit newOrganisationUnit = getModel();
        OrganisationUnit organisationUnit = getModel();
        organisationUnit.setId(2L);

        when(persistenceService.getByName(newOrganisationUnit.getName())).thenReturn(Optional.of(organisationUnit));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(id, newOrganisationUnit));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}
