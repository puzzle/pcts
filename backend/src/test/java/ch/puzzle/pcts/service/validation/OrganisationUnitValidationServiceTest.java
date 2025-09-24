package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import ch.puzzle.pcts.service.persistence.OrganisationUnitPersistenceService;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrganisationUnitValidationServiceTest {
    private AutoCloseable closeable;

    @Mock
    private OrganisationUnitPersistenceService persistenceService;

    @InjectMocks
    private OrganisationUnitValidationService validationService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @DisplayName("Should be successful on validateOnGetById() when id valid")
    @Test
    void shouldBeSuccessfulOnValidateOnGetByIdWhenIdIsValid() {
        long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.of(new OrganisationUnit()));
        assertDoesNotThrow(() -> validationService.validateOnGetById(id));
    }

    @DisplayName("Should throw exception on validateOnGetById() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenIdIsInvalid() {
        long id = -1;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnGetById(id));

        assertEquals("Organisation Unit with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when organisationUnit is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenOrganisationUnitIsValid() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setName("New Organisation Unit");

        assertDoesNotThrow(() -> validationService.validateOnCreate(organisationUnit));
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setName("OrganisationUnit");
        organisationUnit.setId(123L);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(organisationUnit));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameIsNull() {
        OrganisationUnit organisationUnit = new OrganisationUnit();

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(organisationUnit));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.ORGANIZATION_UNIT_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is blank")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameBlank() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setName("");

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(organisationUnit));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.ORGANIZATION_UNIT_NAME_IS_EMPTY, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setName("Existing Organisation unit");

        when(persistenceService.getByName("Existing Organisation unit"))
                .thenReturn(Optional.of(new OrganisationUnit()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(organisationUnit));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.ORGANIZATION_UNIT_NAME_ALREADY_EXISTS, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnDelete() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnDeleteWhenIdIsValid() {
        long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new OrganisationUnit()));

        assertDoesNotThrow(() -> validationService.validateOnDelete(id));
    }

    @DisplayName("Should throw exception on validateOnDelete() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnDeleteIdWhenIdIsInvalid() {
        long id = -1;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnDelete(id));

        assertEquals("Organisation Unit with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnUpdate() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnUpdateWhenIdIsValid() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setName("OrganisationUnit");
        long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new OrganisationUnit()));

        assertDoesNotThrow(() -> validationService.validateOnUpdate(id, organisationUnit));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateIdWhenIdIsInvalid() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        long id = -1;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, organisationUnit));

        assertEquals("Organisation Unit with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNotNull() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setId(123L);
        long id = 1;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new OrganisationUnit()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, organisationUnit));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameIsNull() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        long id = 1;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new OrganisationUnit()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, organisationUnit));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.ORGANIZATION_UNIT_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is blank")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameBlank() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setName("");
        long id = 1;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new OrganisationUnit()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, organisationUnit));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.ORGANIZATION_UNIT_NAME_IS_EMPTY, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setName("Existing Organisation unit");
        long id = 1;

        when(persistenceService.getById(id)).thenReturn(Optional.of(new OrganisationUnit()));
        when(persistenceService.getByName("Existing Organisation unit"))
                .thenReturn(Optional.of(new OrganisationUnit()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, organisationUnit));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.ORGANIZATION_UNIT_NAME_ALREADY_EXISTS, exception.getErrorKey());
    }
}
