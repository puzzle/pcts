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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrganisationUnitValidationServiceTest
        extends
            ValidationBaseServiceTest<OrganisationUnit, OrganisationUnitValidationService> {

    @Mock
    private OrganisationUnitPersistenceService persistenceService;

    @InjectMocks
    private OrganisationUnitValidationService validationService;

    @Override
    OrganisationUnit getModel() {
        return new OrganisationUnit(null, "/team");
    }

    @Override
    OrganisationUnitValidationService getService() {
        return validationService;
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        OrganisationUnit organisationUnit = getModel();

        when(persistenceService.getByName(organisationUnit.getName())).thenReturn(Optional.of(new OrganisationUnit()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(organisationUnit));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.ORGANIZATION_UNIT_NAME_ALREADY_EXISTS, exception.getErrorKey());
    }

    @DisplayName("Should Throw Exception on validateOnUpdate() when name already exists for another organisation unit")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExistsForAnotherOrganisationUnit() {
        Long id = 1L;
        OrganisationUnit newOrganisationUnit = getModel();
        OrganisationUnit organisationUnit = getModel();
        organisationUnit.setId(2L);

        when(persistenceService.getByName(newOrganisationUnit.getName())).thenReturn(Optional.of(organisationUnit));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, newOrganisationUnit));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.ORGANIZATION_UNIT_NAME_ALREADY_EXISTS, exception.getErrorKey());
    }

    @DisplayName("Should not Throw Exception on validateOnUpdate() when name stays the same")
    @Test
    void shouldNotThrowExceptionOnValidateOnUpdateWhenNameStaysTheSame() {
        long id = 1L;

        OrganisationUnit newOrganisationUnit = getModel();

        OrganisationUnit organisationUnit = getModel();
        organisationUnit.setId(id);

        when(persistenceService.getById(id)).thenReturn(Optional.of(newOrganisationUnit));
        when(persistenceService.getByName(newOrganisationUnit.getName())).thenReturn(Optional.of(organisationUnit));

        assertDoesNotThrow(() -> validationService.validateOnUpdate(id, newOrganisationUnit));
    }
}
