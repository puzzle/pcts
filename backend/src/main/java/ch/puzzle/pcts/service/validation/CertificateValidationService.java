package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CERTIFICATE;

import ch.puzzle.pcts.model.certificate.Certificate;
import java.time.LocalDate;
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
        validateDateIsBefore(CERTIFICATE, "completedAt", completedAt, "validUntil", validUntil);
    }
}
