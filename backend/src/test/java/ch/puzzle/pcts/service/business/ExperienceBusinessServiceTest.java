package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.persistence.ExperiencePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceValidationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExperienceBusinessServiceTest
        extends
            BaseBusinessTest<Experience, ExperiencePersistenceService, ExperienceValidationService, ExperienceBusinessService> {

    @Mock
    Experience experience;

    @Mock
    ExperiencePersistenceService persistenceService;

    @Mock
    ExperienceValidationService validationService;

    @InjectMocks
    ExperienceBusinessService businessService;

    @Override
    Experience getModel() {
        return experience;
    }

    @Override
    ExperiencePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    ExperienceValidationService getValidationService() {
        return validationService;
    }

    @Override
    ExperienceBusinessService getBusinessService() {
        return businessService;
    }
}
