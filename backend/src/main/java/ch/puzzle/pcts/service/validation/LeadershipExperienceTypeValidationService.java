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
public class LeadershipExperienceTypeValidationService extends ValidationBase<CertificateType> {

    private final CertificateTypePersistenceService persistenceService;

    public LeadershipExperienceTypeValidationService(CertificateTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(CertificateType leadershipExperience) {
        super.validateOnCreate(leadershipExperience);
        validateCertificateKind(leadershipExperience.getCertificateKind());
        if (UniqueNameValidationUtil.nameAlreadyUsed(leadershipExperience.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    @Override
    public void validateOnUpdate(Long id, CertificateType leadershipExperience) {
        super.validateOnUpdate(id, leadershipExperience);
        validateCertificateKind(leadershipExperience.getCertificateKind());
        if (UniqueNameValidationUtil
                .nameExcludingIdAlreadyUsed(id, leadershipExperience.getName(), persistenceService::getByName)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        }
    }

    public void validateCertificateKind(CertificateKind certificatekind) {
        if (!certificatekind.isLeadershipExperienceType()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "CertificateType.certificateKind is not leadership experience.",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }
}
