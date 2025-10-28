package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateValidationServiceTest extends ValidationBaseServiceTest<Certificate, CertificateValidationService> {

    @InjectMocks
    CertificateValidationService service;

    @Mock
    private CertificatePersistenceService persistenceService;

    @Override
    Certificate getModel() {
        return new Certificate(null,
                               "Certificate",
                               BigDecimal.valueOf(10),
                               "Comment",
                               Set.of(new Tag(null, "Tag")),
                               CertificateType.CERTIFICATE);
    }

    @Override
    CertificateValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNull() {
        Certificate certificate = getModel();
        certificate.setPoints(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(certificate));

        assertEquals("Certificate.points must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNegative() {
        Certificate certificate = getModel();
        certificate.setPoints(new BigDecimal("-1"));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(certificate));

        assertEquals("Certificate.points must not be negative.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNull() {
        Certificate certificate = getModel();
        certificate.setPoints(null);
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, certificate));

        assertEquals("Certificate.points must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNegative() {
        Certificate certificate = getModel();
        certificate.setPoints(new BigDecimal("-1"));
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, certificate));

        assertEquals("Certificate.points must not be negative.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnGetById() when certificate type is not certificate")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenCertificateTypeIsNotCertificate() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service
                                                       .validateCertificateType(CertificateType.LEADERSHIP_TRAINING));

        assertEquals("Certificate.CertificateType is not certificate.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        Certificate leadershipExperience = getModel();

        when(persistenceService.getByName(leadershipExperience.getName())).thenReturn(Optional.of(new Certificate()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperience));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        Certificate certificate = getModel();
        Certificate newCertificate = getModel();
        certificate.setId(2L);

        when(persistenceService.getByName(newCertificate.getName())).thenReturn(Optional.of(certificate));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, newCertificate));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}
