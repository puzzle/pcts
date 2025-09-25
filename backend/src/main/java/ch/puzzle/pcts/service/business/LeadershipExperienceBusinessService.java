package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.LeadershipExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceBusinessService {
    private final LeadershipExperienceValidationService validationService;
    private final LeadershipExperiencePersistenceService persistenceService;

    public LeadershipExperienceBusinessService(LeadershipExperienceValidationService leadershipExperienceValidationService,
                                               LeadershipExperiencePersistenceService leadershipExperiencePersistenceService) {
        this.validationService = leadershipExperienceValidationService;
        this.persistenceService = leadershipExperiencePersistenceService;
    }

    public Certificate create(Certificate leadershipExperience) {
        validationService.validateOnCreate(leadershipExperience);
        return persistenceService.create(leadershipExperience);
    }

    public Certificate getById(long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     "Leadership experience with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
    }

    public List<Certificate> getAll() {
        return persistenceService.getAll();
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
