package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationBusinessServiceTest
        extends
            BaseBusinessTest<Calculation, CalculationPersistenceService, CalculationValidationService, CalculationBusinessService> {

    private static final Long ID = 1L;

    @Mock
    private CalculationValidationService validationService;

    @Mock
    private CalculationPersistenceService persistenceService;

    @Mock
    private ExperienceCalculationBusinessService experienceBusinessService;

    @Mock
    private DegreeCalculationBusinessService degreeBusinessService;

    @Mock
    private CertificateCalculationBusinessService certificateBusinessService;

    @Mock
    private Calculation calculation;

    @InjectMocks
    private CalculationBusinessService businessService;

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

    @Override
    @DisplayName("Should get calculation by id and set total points")
    @Test
    void shouldGetById() {
        Calculation calc = new Calculation();

        when(persistenceService.getById(ID)).thenReturn(calc);
        when(experienceBusinessService.getExperiencePoints(ID)).thenReturn(BigDecimal.ONE);
        when(degreeBusinessService.getDegreePoints(ID)).thenReturn(BigDecimal.ONE);
        when(certificateBusinessService.getCertificatePoints(ID)).thenReturn(BigDecimal.ONE);

        Calculation result = businessService.getById(ID);

        assertEquals(calc, result);
        assertEquals(BigDecimal.valueOf(3), result.getPoints());
        verify(persistenceService).getById(ID);
        verify(experienceBusinessService).getExperiencePoints(ID);
        verify(degreeBusinessService).getDegreePoints(ID);
        verify(certificateBusinessService).getCertificatePoints(ID);

    }

    @Override
    @DisplayName("Should create calculation with experiences")
    @Test
    void shouldCreate() {
        super.shouldCreate();
        verify(experienceBusinessService).createExperienceCalculations(calculation);
        verify(certificateBusinessService).createCertificateCalculations(calculation);
        verify(degreeBusinessService).createDegreeCalculations(calculation);
    }

    @Override
    @DisplayName("Should update calculation with experiences")
    @Test
    void shouldUpdate() {
        super.shouldUpdate();
        verify(validationService).validateOnUpdate(ID, calculation);
        verify(experienceBusinessService).updateExperienceCalculations(calculation);
        verify(certificateBusinessService).updateCertificateCalculations(calculation);
        verify(degreeBusinessService).updateDegreeCalculations(calculation);
    }
}
