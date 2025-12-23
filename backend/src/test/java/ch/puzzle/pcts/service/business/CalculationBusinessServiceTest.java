package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationBusinessServiceTest
        extends
            BaseBusinessTest<Calculation, CalculationPersistenceService, CalculationValidationService, CalculationBusinessService> {

    @Mock
    Calculation calculation;

    @Mock
    CalculationPersistenceService persistenceService;

    @Mock
    CalculationValidationService validationService;

    @InjectMocks
    CalculationBusinessService businessService;

    @Override
    Calculation getModel() {
        return calculation;
    }

    @Override
    CalculationPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    CalculationValidationService getValidationService() {
        return validationService;
    }

    @Override
    CalculationBusinessService getBusinessService() {
        return businessService;
    }
}
