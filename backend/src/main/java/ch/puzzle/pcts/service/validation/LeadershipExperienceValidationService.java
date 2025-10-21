package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceValidationService extends ValidationBase<Certificate> {
    @Override
    public void validateOnCreate(Certificate leadershipExperience) {
        super.validateOnCreate(leadershipExperience);
        validateCertificateType(leadershipExperience.getCertificateType());
    }

    @Override
    public void validateOnUpdate(Long id, Certificate leadershipExperience) {
        super.validateOnUpdate(id, leadershipExperience);
        validateCertificateType(leadershipExperience.getCertificateType());
    }

    public void validateCertificateType(CertificateType certificateType) {
        if (!certificateType.isLeadershipExperience()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate.CertificateType is not leadership experience.",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }
}
