package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.model.error.ErrorKey;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateValidationServiceTest extends ValidationBaseServiceTest<Certificate, CertificateValidationService> {

    @InjectMocks
    CertificateValidationService service;

    @Override
    Certificate getModel() {
        return new Certificate(null, "Certificate", BigDecimal.valueOf(10), "Comment", Set.of(new Tag(null, "Tag")));
    }

    @Override
    void validate() {

    }

    @Override
    CertificateValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNull() {
        Certificate certificate = new Certificate();
        certificate.setName("Valid Certificate");
        certificate.setComment("Valid Comment");

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(certificate));

        assertEquals("Certificate.points must not be null", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNegative() {
        Certificate certificate = new Certificate();
        certificate.setName("Valid Certificate");
        certificate.setComment("Valid Comment");
        certificate.setPoints(new BigDecimal("-1.0"));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(certificate));

        assertEquals("Certificate.points must not be negative", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNull() {
        Certificate certificate = new Certificate();
        certificate.setName("Valid Certificate");
        certificate.setComment("Valid Comment");
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, certificate));

        assertEquals("Certificate.points must not be null", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNegative() {
        Certificate certificate = new Certificate();
        certificate.setName("Valid Certificate");
        certificate.setComment("Valid Comment");
        certificate.setPoints(new BigDecimal("-1.0"));
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, certificate));

        assertEquals("Certificate.points must not be negative", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}
