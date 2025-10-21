package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceValidationServiceTest
        extends
            ValidationBaseServiceTest<Certificate, LeadershipExperienceValidationService> {

    @InjectMocks
    LeadershipExperienceValidationService service;

    @Override
    Certificate getModel() {
        return new Certificate(null,
                               "Valid Leadership Experience",
                               BigDecimal.valueOf(10),
                               "Comment",
                               CertificateType.YOUTH_AND_SPORT);
    }

    @Override
    LeadershipExperienceValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNull() {
        Certificate leadershipExperience = getModel();
        leadershipExperience.setPoints(null);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperience));

        assertEquals("Certificate.points must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNegative() {
        Certificate leadershipExperience = getModel();
        leadershipExperience.setPoints(new BigDecimal("-1"));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnCreate(leadershipExperience));

        assertEquals("Certificate.points must not be negative.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNull() {
        Certificate leadershipExperience = getModel();
        leadershipExperience.setPoints(null);
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(id, leadershipExperience));

        assertEquals("Certificate.points must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNegative() {
        Certificate leadershipExperience = getModel();
        leadershipExperience.setPoints(new BigDecimal("-1"));
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(id, leadershipExperience));

        assertEquals("Certificate.points must not be negative.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnGetById() when certificate type is not leadership experience")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenCertificateTypeIsNotCertificate() {
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateCertificateType(CertificateType.CERTIFICATE));

        assertEquals("Certificate.CertificateType is not leadership experience.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}
