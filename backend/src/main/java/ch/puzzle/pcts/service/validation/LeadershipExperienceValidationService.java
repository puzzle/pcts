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
public class LeadershipExperienceValidationService extends ValidationBase<Certificate> {

    private final CertificatePersistenceService persistenceService;

    public LeadershipExperienceValidationService(CertificatePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(Certificate leadershipExperience) {
        super.validateOnCreate(leadershipExperience);
        validateCertificateType(leadershipExperience.getCertificateType());
        if (UniqueNameValidationUtil.nameAlreadyUsed(leadershipExperience.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    @Override
    public void validateOnUpdate(Long id, Certificate leadershipExperience) {
        super.validateOnUpdate(id, leadershipExperience);
        validateCertificateType(leadershipExperience.getCertificateType());
        if (UniqueNameValidationUtil
                .nameExcludingSelfAlredyUsed(id, leadershipExperience.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    public void validateCertificateType(CertificateType certificateType) {
        if (!certificateType.isLeadershipExperience()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate.CertificateType is not leadership experience.",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }
}
