package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeValidationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeBusinessServiceTest
        extends
            BaseBusinessTest<Degree, DegreePersistenceService, DegreeValidationService, DegreeBusinessService> {

    @Mock
    Degree degree;

    @Mock
    DegreePersistenceService persistenceService;

    @Mock
    DegreeValidationService validationService;

    @InjectMocks
    DegreeBusinessService businessService;

    @Override
    Degree getModel() {
        return degree;
    }

    @Override
    DegreePersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    DegreeValidationService getValidationService() {
        return validationService;
    }

    @Override
    DegreeBusinessService getBusinessService() {
        return businessService;
    }
}
