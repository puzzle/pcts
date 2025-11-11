package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.util.UniqueNameValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeValidationService extends ValidationBase<CertificateType> {

    private final CertificateTypePersistenceService persistenceService;

    public CertificateTypeValidationService(CertificateTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnUpdate(Long id, CertificateType certificate) {
        super.validateOnUpdate(id, certificate);
        validateCertificateKind(certificate.getCertificateKind());
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, certificate.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    @Override
    public void validateOnCreate(CertificateType certificate) {
        super.validateOnCreate(certificate);
        validateCertificateKind(certificate.getCertificateKind());
        if (UniqueNameValidationUtil.nameAlreadyUsed(certificate.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    public void validateCertificateKind(CertificateKind certificateKind) {
        if (certificateKind != CertificateKind.CERTIFICATE) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate.CertificateType is not certificate.",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }
}
