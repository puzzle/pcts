package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.service.persistence.LeadershipExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceBusinessService extends BusinessBase<LeadershipExperience> {

    LeadershipExperienceBusinessService(LeadershipExperiencePersistenceService leadershipExperiencePersistenceService,
                                        LeadershipExperienceValidationService validationService) {
        super(validationService, leadershipExperiencePersistenceService);
    }
}
