package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateValidationService extends ValidationBase<Certificate> {

    @Override
    public void validateOnUpdate(Long id, Certificate certificate) {
        super.validateOnUpdate(id, certificate);
        validateCertificateType(certificate.getCertificateType());
    }

    @Override
    public void validateOnCreate(Certificate certificate) {
        super.validateOnCreate(certificate);
        validateCertificateType(certificate.getCertificateType());
    }

    public void validateCertificateType(CertificateType certificateType) {
        if (certificateType != CertificateType.CERTIFICATE) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate type is not certificate.",
                                    ErrorKey.CERTIFICATE_TYPE_IS_NOT_CERTIFICATE);
        }
    }
}
