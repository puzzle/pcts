package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.service.persistence.LeadershipExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceValidationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeadershipExperienceBusinessServiceTest
        extends
            BaseBusinessTest<LeadershipExperience, LeadershipExperiencePersistenceService, LeadershipExperienceValidationService, LeadershipExperienceBusinessService> {

    @Mock
    private LeadershipExperience leadershipExperience;

    @Mock
    private LeadershipExperiencePersistenceService persistenceService;

    @Mock
    private LeadershipExperienceValidationService validationService;

    @InjectMocks
    private LeadershipExperienceBusinessService businessService;

    @Override
    LeadershipExperience getModel() {
        return leadershipExperience;
    }

    @Override
    LeadershipExperiencePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    LeadershipExperienceValidationService getValidationService() {
        return validationService;
    }

    @Override
    LeadershipExperienceBusinessService getBusinessService() {
        return businessService;
    }
}
