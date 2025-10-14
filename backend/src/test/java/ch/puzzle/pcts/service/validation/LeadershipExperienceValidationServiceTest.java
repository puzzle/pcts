package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceValidationServiceTest {

    @Mock
    private CertificatePersistenceService persistenceService;

    @Mock
    Certificate leadershipExperience;

    @InjectMocks
    private LeadershipExperienceValidationService validationService;

    @DisplayName("Should be successful on validateOnGetById() when id valid")
    @Test
    void shouldBeSuccessfulOnValidateOnGetByIdWhenIdIsValid() {
        long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(mock(Certificate.class)));

        assertDoesNotThrow(() -> validationService.validateOnGetById(id));
    }

    @DisplayName("Should throw exception on validateOnGetById() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenIdIsInvalid() {
        long id = -1L;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnGetById(id));

        assertEquals("LeadershipExperience with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when leadershipExperience is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenLeadershipExperienceIsValid() {
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("New leadershipExperience");
        when(leadershipExperience.getPoints()).thenReturn(BigDecimal.valueOf(5));
        when(leadershipExperience.getCertificateType()).thenReturn(CertificateType.LEADERSHIP_TRAINING);

        assertDoesNotThrow(() -> validationService.validateOnCreate(leadershipExperience));
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        when(leadershipExperience.getId()).thenReturn(123L);

        PCTSException exception = assertThrows(PCTSException.class,
                () -> validationService.validateOnCreate(leadershipExperience));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameIsNull() {
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn(null);

        PCTSException exception = assertThrows(PCTSException.class,
                () -> validationService.validateOnCreate(leadershipExperience));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.LEADERSHIP_EXPERIENCE_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is blank")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameBlank() {
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("");

        PCTSException exception = assertThrows(PCTSException.class,
                () -> validationService.validateOnCreate(leadershipExperience));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.LEADERSHIP_EXPERIENCE_NAME_IS_EMPTY, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsIsNull() {
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("Valid name");
        when(leadershipExperience.getPoints()).thenReturn(null);

        PCTSException exception = assertThrows(PCTSException.class,
                () -> validationService.validateOnCreate(leadershipExperience));

        assertEquals("Points value must not be null.", exception.getReason());
        assertEquals(ErrorKey.LEADERSHIP_EXPERIENCE_POINTS_ARE_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points is negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsNegative() {
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("Valid name");
        when(leadershipExperience.getPoints()).thenReturn(BigDecimal.valueOf(-5));

        PCTSException exception = assertThrows(PCTSException.class,
                () -> validationService.validateOnCreate(leadershipExperience));

        assertEquals("Points value must not be negative.", exception.getReason());
        assertEquals(ErrorKey.LEADERSHIP_EXPERIENCE_POINTS_ARE_NEGATIVE, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when points valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenPointsValid() {
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("Valid name");
        when(leadershipExperience.getPoints()).thenReturn(new BigDecimal("99.99"));
        when(leadershipExperience.getCertificateType()).thenReturn(CertificateType.LEADERSHIP_TRAINING);

        assertDoesNotThrow(() -> validationService.validateOnCreate(leadershipExperience));
    }

    @DisplayName("Should throw exception on validateOnCreate() when certificate type is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenCertificateTypeIsNull() {
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("Valid name");
        when(leadershipExperience.getPoints()).thenReturn(BigDecimal.valueOf(50));
        when(leadershipExperience.getCertificateType()).thenReturn(null);

        PCTSException exception = assertThrows(PCTSException.class,
                () -> validationService.validateOnCreate(leadershipExperience));

        assertEquals("Certificate type must not be null.", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_TYPE_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when certificate type is certificate")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenCertificateTypeIsCertificate() {
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("Valid name");
        when(leadershipExperience.getPoints()).thenReturn(BigDecimal.valueOf(50));
        when(leadershipExperience.getCertificateType()).thenReturn(CertificateType.CERTIFICATE);

        PCTSException exception = assertThrows(PCTSException.class,
                () -> validationService.validateOnCreate(leadershipExperience));

        assertEquals("Certificate type is not a leadership experience.", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_TYPE_IS_NOT_A_LEADERSHIP_EXPERIENCE, exception.getErrorKey());
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

        assertEquals("LeadershipExperience with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnUpdate() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnUpdateWhenIdIsValid() {
        long id = 1L;
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("Valid name");
        when(leadershipExperience.getPoints()).thenReturn(BigDecimal.valueOf(5));
        when(leadershipExperience.getCertificateType()).thenReturn(CertificateType.LEADERSHIP_TRAINING);

        when(persistenceService.getById(id)).thenReturn(Optional.of(mock(Certificate.class)));

        assertDoesNotThrow(() -> validationService.validateOnUpdate(id, leadershipExperience));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateIdWhenIdIsInvalid() {
        long id = -1L;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, leadershipExperience));

        assertEquals("LeadershipExperience with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is not null in leadershipExperience")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNotNull() {
        long id = 1L;
        when(leadershipExperience.getId()).thenReturn(123L);
        when(persistenceService.getById(id)).thenReturn(Optional.of(mock(Certificate.class)));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, leadershipExperience));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameIsNull() {
        long id = 1L;
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn(null);

        when(persistenceService.getById(id)).thenReturn(Optional.of(mock(Certificate.class)));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, leadershipExperience));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.LEADERSHIP_EXPERIENCE_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when certificate type is certificate")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenCertificateTypeIsCertificate() {
        long id = 1L;
        when(leadershipExperience.getId()).thenReturn(null);
        when(leadershipExperience.getName()).thenReturn("Valid name");
        when(leadershipExperience.getPoints()).thenReturn(BigDecimal.valueOf(50));
        when(leadershipExperience.getCertificateType()).thenReturn(CertificateType.CERTIFICATE);

        when(persistenceService.getById(id)).thenReturn(Optional.of(mock(Certificate.class)));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, leadershipExperience));

        assertEquals("Certificate type is not a leadership experience.", exception.getReason());
        assertEquals(ErrorKey.CERTIFICATE_TYPE_IS_NOT_A_LEADERSHIP_EXPERIENCE, exception.getErrorKey());
    }
}
