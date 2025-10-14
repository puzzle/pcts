package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateValidationService {
    private final CertificatePersistenceService persistenceService;

    public CertificateValidationService(CertificatePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnCreate(Certificate certificate) {
        validateIfIdIsNull(certificate.getId());
        validateName(certificate.getName());
        validatePoints(certificate.getPoints());
        validateCertificateType(certificate.getCertificateType());
    }

    public void validateOnGetById(Long id) {
        validateIfExists(id);
    }

    public void validateOnUpdate(Long id, Certificate certificate) {
        validateIfExists(id);
        validateIfIdIsNull(certificate.getId());
        validateName(certificate.getName());
        validatePoints(certificate.getPoints());
        validateCertificateType(certificate.getCertificateType());
    }

    public void validateOnDelete(Long id) {
        validateIfExists(id);
    }

    public void validateCertificateType(CertificateType certificateType) {
        if (certificateType == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate type must not be null.",
                                    ErrorKey.CERTIFICATE_TYPE_IS_NULL);
        }
        if (certificateType != CertificateType.CERTIFICATE) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate type is not certificate.",
                                    ErrorKey.CERTIFICATE_TYPE_IS_NOT_CERTIFICATE);
        }
    }

    private void validateIfIdIsNull(Long id) {
        if (id != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.ID_IS_NOT_NULL);
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name must not be null", ErrorKey.CERTIFICATE_NAME_IS_NULL);
        }

        if (name.isBlank()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Name must not be empty",
                                    ErrorKey.CERTIFICATE_NAME_IS_EMPTY);
        }
    }

    private void validateIfExists(Long id) {
        persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Certificate with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    private void validatePoints(BigDecimal points) {
        if (points == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Points value must not be null.",
                                    ErrorKey.CERTIFICATE_POINTS_ARE_NULL);
        }

        if (points.signum() < 0) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Points value must not be negative.",
                                    ErrorKey.CERTIFICATE_POINTS_ARE_NEGATIVE);
        }
    }
}
