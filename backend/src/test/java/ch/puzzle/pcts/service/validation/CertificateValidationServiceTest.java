package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CertificateValidationServiceTest {
    private AutoCloseable closeable;

    @Mock
    private CertificatePersistenceService persistenceService;

    @InjectMocks
    private CertificateValidationService validationService;

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
        when(persistenceService.getById(id)).thenReturn(Optional.of(new Certificate()));

        assertDoesNotThrow(() -> validationService.validateOnGetById(id));
    }

    @DisplayName("Should throw exception on validateOnGetById() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenIdIsInvalid() {
        long id = -1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnGetById(id));

        assertEquals("Certificate with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when certificate is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenCertificateIsValid() {
        Certificate certificate = new Certificate();
        certificate.setName("New certificate");
        certificate.setPoints(BigDecimal.valueOf(5));

        assertDoesNotThrow(() -> validationService.validateOnCreate(certificate));
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        Certificate certificate = new Certificate();
        certificate.setId(123L);
        certificate.setName("Valid name");

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(certificate));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameIsNull() {
        Certificate certificate = new Certificate();

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(certificate));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is blank")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameBlank() {
        Certificate certificate = new Certificate();
        certificate.setName("");

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(certificate));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_NAME_IS_EMPTY, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsIsNull() {
        Certificate certificate = new Certificate();
        certificate.setName("Valid name");
        certificate.setPoints(null);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(certificate));

        assertEquals("Points value must not be null.", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_POINTS_ARE_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points is negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsNegative() {
        Certificate certificate = new Certificate();
        certificate.setName("Valid name");
        certificate.setPoints(BigDecimal.valueOf(-5));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(certificate));

        assertEquals("Points value must not be negative.", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_POINTS_ARE_NEGATIVE, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when points is valid (two decimals or fewer)")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenPointsValid() {
        Certificate certificate = new Certificate();
        certificate.setName("Valid name");
        certificate.setPoints(new BigDecimal("99.99"));

        assertDoesNotThrow(() -> validationService.validateOnCreate(certificate));
    }

    @DisplayName("Should be successful on validateOnCreate() when points is integer value")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenPointsIsInteger() {
        Certificate certificate = new Certificate();
        certificate.setName("Valid name");
        certificate.setPoints(BigDecimal.valueOf(50));

        assertDoesNotThrow(() -> validationService.validateOnCreate(certificate));
    }

    @DisplayName("Should be successful on validateOnDelete() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnDeleteWhenIdIsValid() {
        long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new Certificate()));

        assertDoesNotThrow(() -> validationService.validateOnDelete(id));
    }

    @DisplayName("Should throw exception on validateOnDelete() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnDeleteIdWhenIdIsInvalid() {
        long id = -1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnDelete(id));

        assertEquals("Certificate with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnUpdate() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnUpdateWhenIdIsValid() {
        long id = 1L;
        Certificate certificate = new Certificate();
        certificate.setName("Valid name");
        certificate.setPoints(BigDecimal.valueOf(5));

        when(persistenceService.getById(id)).thenReturn(Optional.of(new Certificate()));

        assertDoesNotThrow(() -> validationService.validateOnUpdate(id, certificate));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateIdWhenIdIsInvalid() {
        long id = -1L;
        Certificate certificate = new Certificate();
        certificate.setName("Valid name");

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, certificate));

        assertEquals("Certificate with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is not null in certificate")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNotNull() {
        long id = 1L;
        Certificate certificate = new Certificate();
        certificate.setId(123L);
        certificate.setName("Valid name");

        when(persistenceService.getById(id)).thenReturn(Optional.of(new Certificate()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, certificate));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameIsNull() {
        long id = 1L;
        Certificate certificate = new Certificate();

        when(persistenceService.getById(id)).thenReturn(Optional.of(new Certificate()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, certificate));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is blank")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameBlank() {
        long id = 1L;
        Certificate certificate = new Certificate();
        certificate.setName("");

        when(persistenceService.getById(id)).thenReturn(Optional.of(new Certificate()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, certificate));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_NAME_IS_EMPTY, exception.getErrorKey());
    }
}
