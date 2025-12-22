package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationBusinessServiceTest {

    private static final Long ID = 1L;

    @Mock
    private CalculationValidationService validationService;

    @Mock
    private CalculationPersistenceService persistenceService;

    @Mock
    private Calculation calculation;

    @InjectMocks
    private CalculationBusinessService businessService;

    @DisplayName("Should get calculation by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(ID)).thenReturn(calculation);

        Calculation result = businessService.getById(ID);

        assertEquals(calculation, result);
        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should create calculation")
    @Test
    void shouldCreate() {
        when(persistenceService.save(calculation)).thenReturn(calculation);

        Calculation result = businessService.create(calculation);

        assertEquals(calculation, result);
        verify(validationService).validateOnCreate(calculation);
        verify(persistenceService).save(calculation);
    }

    @DisplayName("Should update calculation")
    @Test
    void shouldUpdate() {
        when(persistenceService.getById(ID)).thenReturn(calculation);
        when(persistenceService.save(calculation)).thenReturn(calculation);

        Calculation result = businessService.update(ID, calculation);

        assertEquals(calculation, result);
        verify(calculation).setId(ID);
        verify(validationService).validateOnUpdate(ID, calculation);
        verify(persistenceService).save(calculation);
    }
}
