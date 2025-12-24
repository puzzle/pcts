package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import ch.puzzle.pcts.service.validation.OrganisationUnitValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganisationUnitBusinessServiceTest
        extends
            BaseBusinessTest<OrganisationUnit, OrganisationUnitPersistenceService, OrganisationUnitValidationService, OrganisationUnitBusinessService> {

    @Mock
    private OrganisationUnit organisationUnit;

    @Mock
    private List<OrganisationUnit> organisationUnits;

    @Mock
    private OrganisationUnitPersistenceService persistenceService;

    @Mock
    private OrganisationUnitValidationService validationService;

    @InjectMocks
    OrganisationUnitBusinessService businessService;

    @Override
    OrganisationUnit getModel() {
        return organisationUnit;
    }

    @Override
    OrganisationUnitPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    OrganisationUnitValidationService getValidationService() {
        return validationService;
    }

    @Override
    OrganisationUnitBusinessService getBusinessService() {
        return businessService;
    }

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(organisationUnits);
        when(organisationUnits.size()).thenReturn(2);

        List<OrganisationUnit> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(organisationUnits, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<OrganisationUnit> result = businessService.getAll();

        assertEquals(0, result.size());
    }
}
