package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import java.util.Optional;
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
        validateNameUniqueness(leadershipExperience.getName());
    }

    @Override
    public void validateOnUpdate(Long id, Certificate leadershipExperience) {
        super.validateOnUpdate(id, leadershipExperience);
        validateCertificateType(leadershipExperience.getCertificateType());
        validateNameUniqueExcludingSelf(id, leadershipExperience.getName());
    }

    public void validateCertificateType(CertificateType certificateType) {
        if (!certificateType.isLeadershipExperience()) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Certificate.CertificateType is not leadership experience.",
                                    ErrorKey.INVALID_ARGUMENT);
        }
    }

    private void validateNameUniqueness(String name) {
        persistenceService.getByName(name).ifPresent(leadershipExperience -> {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        });
    }

    private void validateNameUniqueExcludingSelf(Long id, String name) {
        Optional<Certificate> existingRole = persistenceService.getByName(name);
        existingRole.ifPresent(leadershipExperience -> {
            if (!leadershipExperience.getId().equals(id)) {
                throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
            }
        });
    }
}
