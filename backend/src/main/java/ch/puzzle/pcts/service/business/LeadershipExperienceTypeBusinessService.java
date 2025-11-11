package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceTypeValidationService;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypeBusinessService {
    private final LeadershipExperienceTypeValidationService validationService;
    private final CertificateTypePersistenceService persistenceService;

    public LeadershipExperienceTypeBusinessService(LeadershipExperienceTypeValidationService leadershipExperienceTypeValidationService,
                                                   CertificateTypePersistenceService certificateTypePersistenceService) {
        this.validationService = leadershipExperienceTypeValidationService;
        this.persistenceService = certificateTypePersistenceService;
    }

    public CertificateType create(CertificateType leadershipExperience) {
        validationService.validateOnCreate(leadershipExperience);

        return persistenceService.save(leadershipExperience);
    }

    public CertificateType getById(Long id) {
        validationService.validateOnGetById(id);

        Optional<CertificateType> optionalLeadershipExperienceType = persistenceService.getById(id);

        if (optionalLeadershipExperienceType.isPresent()) {
            CertificateType leadershipExperience = optionalLeadershipExperienceType.get();
            validationService.validateCertificateKind(leadershipExperience.getCertificateKind());
            return leadershipExperience;
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    "LeadershipExperience type with id: " + id + " does not exist.",
                                    ErrorKey.NOT_FOUND);
        }
    }

    public List<CertificateType> getAll() {
        return persistenceService.getAllLeadershipExperienceTypes();
    }

    public CertificateType update(Long id, CertificateType leadershipExperienceType) {
        validationService.validateOnUpdate(id, leadershipExperienceType);
        leadershipExperienceType.setId(id);
        return persistenceService.save(leadershipExperienceType);
    }
    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }

}
