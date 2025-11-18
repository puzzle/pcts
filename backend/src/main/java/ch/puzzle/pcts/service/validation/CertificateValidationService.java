package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.mode.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateValidationService extends ValidationBase<Certificate> {
    @Override
    public void validateOnCreate(Certificate certificate) {
        super.validateOnCreate(certificate);
        validateCompletedAtIsBeforeValidUntil(certificate.getCompletedAt(), certificate.getValidUntil());
    }
    @Override
    public void validateOnUpdate(Long id, Certificate certificate) {
        super.validateOnUpdate(id, certificate);
        validateCompletedAtIsBeforeValidUntil(certificate.getCompletedAt(), certificate.getValidUntil());
    }

    public void validateCompletedAtIsBeforeValidUntil(LocalDate completedAt, LocalDate validUntil) {
        if (validUntil != null && completedAt.isAfter(validUntil)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate.completedAT must be after the validUntil date, given validUntil: "
                                                            + validUntil + " and completedAt: " + completedAt + ".",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }
}
