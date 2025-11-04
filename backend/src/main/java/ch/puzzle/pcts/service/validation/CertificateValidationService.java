package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateValidationService extends ValidationBase<Certificate> {

    private final CertificatePersistenceService persistenceService;

    public CertificateValidationService(CertificatePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnUpdate(Long id, Certificate certificate) {
        super.validateOnUpdate(id, certificate);
        validateCertificateType(certificate.getCertificateType());
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, certificate.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    @Override
    public void validateOnCreate(Certificate certificate) {
        super.validateOnCreate(certificate);
        validateCertificateType(certificate.getCertificateType());
        if (UniqueNameValidationUtil.nameAlreadyUsed(certificate.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    public void validateCertificateType(CertificateType certificateType) {
        if (certificateType != CertificateType.CERTIFICATE) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate.CertificateType is not certificate.",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }
}
