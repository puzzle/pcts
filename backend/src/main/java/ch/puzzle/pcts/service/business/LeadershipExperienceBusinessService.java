package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificatePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceBusinessService {
    private final LeadershipExperienceValidationService validationService;
    private final CertificatePersistenceService persistenceService;

    public LeadershipExperienceBusinessService(LeadershipExperienceValidationService leadershipExperienceValidationService,
                                               CertificatePersistenceService certificatePersistenceService) {
        this.validationService = leadershipExperienceValidationService;
        this.persistenceService = certificatePersistenceService;
    }

    public Certificate create(Certificate leadershipExperience) {
        validationService.validateOnCreate(leadershipExperience);

        return persistenceService.create(leadershipExperience);
    }

    public Certificate getById(long id) {
        validationService.validateOnGetById(id);

        Optional<Certificate> optionalLeadershipExperience = persistenceService.getById(id);

        if (optionalLeadershipExperience.isPresent()) {
            Certificate leadershipExperience = optionalLeadershipExperience.get();
            validationService.validateCertificateType(leadershipExperience.getCertificateType());
            return leadershipExperience;
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    "LeadershipExperience with id: " + id + " does not exist.",
                                    ErrorKey.NOT_FOUND);
        }
    }

    public List<Certificate> getAll() {
        return persistenceService.getAllLeadershipExperiences();
    }

    public Certificate update(Long id, Certificate leadershipExperience) {
        validationService.validateOnUpdate(id, leadershipExperience);
        return persistenceService.update(id, leadershipExperience);
    }
    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }

}
