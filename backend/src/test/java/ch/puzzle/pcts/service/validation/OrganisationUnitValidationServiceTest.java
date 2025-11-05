package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganisationUnitValidationServiceTest
        extends
            ValidationBaseServiceTest<OrganisationUnit, OrganisationUnitValidationService> {

    @Mock
    private OrganisationUnitPersistenceService persistenceService;

    @InjectMocks
    private OrganisationUnitValidationService service;

    @Override
    OrganisationUnit getValidModel() {
        return new OrganisationUnit(null, "Organisation Unit");
    }

    @Override
    OrganisationUnitValidationService getService() {
        return service;
    }

    private static OrganisationUnit createOrganisationUnit(String name) {
        OrganisationUnit o = new OrganisationUnit();
        o.setName(name);
        return o;
    }

    static Stream<Arguments> invalidModelProvider() {
        String tooLongName = new String(new char[251]).replace("\0", "s");

        return Stream
                .of(Arguments.of(createOrganisationUnit(null), "OrganisationUnit.name must not be null."),
                    Arguments.of(createOrganisationUnit(""), "OrganisationUnit.name must not be blank."),
                    Arguments.of(createOrganisationUnit("  "), "OrganisationUnit.name must not be blank."),
                    Arguments
                            .of(createOrganisationUnit("S"),
                                "OrganisationUnit.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createOrganisationUnit("  S "),
                                "OrganisationUnit.name size must be between 2 and 250, given S."),
                    Arguments
                            .of(createOrganisationUnit(tooLongName),
                                String
                                        .format("OrganisationUnit.name size must be between 2 and 250, given %s.",
                                                tooLongName)));

    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        OrganisationUnit organisationUnit = getValidModel();

        when(persistenceService.getByName(organisationUnit.getName())).thenReturn(Optional.of(new OrganisationUnit()));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(organisationUnit));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        OrganisationUnit newOrganisationUnit = getValidModel();
        OrganisationUnit organisationUnit = getValidModel();
        organisationUnit.setId(2L);

        when(persistenceService.getByName(newOrganisationUnit.getName())).thenReturn(Optional.of(organisationUnit));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(id, newOrganisationUnit));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallAllMethodsOnValidateOnCreateWhenValid() {
        OrganisationUnit organisationUnit = getValidModel();

        OrganisationUnitValidationService spyService = spy(service);
        doNothing().when((ValidationBase<OrganisationUnit>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(organisationUnit);

        verify(spyService).validateOnCreate(organisationUnit);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallAllMethodsOnValidateOnUpdateWhenValid() {
        Long id = 1L;
        OrganisationUnit organisationUnit = getValidModel();

        OrganisationUnitValidationService spyService = spy(service);
        doNothing().when((ValidationBase<OrganisationUnit>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, organisationUnit);

        verify(spyService).validateOnUpdate(id, organisationUnit);
        verifyNoMoreInteractions(persistenceService);
    }
}
