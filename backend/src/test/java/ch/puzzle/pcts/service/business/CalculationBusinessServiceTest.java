package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private ExperienceCalculationBusinessService experienceBusinessService;

    @Mock
    private DegreeCalculationBusinessService degreeBusinessService;

    @Mock
    private CertificateCalculationBusinessService certificateBusinessService;

    @Mock
    private Calculation calculation;

    @InjectMocks
    private CalculationBusinessService businessService;

    @DisplayName("Should get calculation by id and set total points")
    @Test
    void shouldGetById() {
        Calculation calc = new Calculation();

        when(persistenceService.getById(ID)).thenReturn(Optional.of(calc));
        when(experienceBusinessService.getExperiencePoints(ID)).thenReturn(BigDecimal.ONE);
        when(degreeBusinessService.getDegreePoints(ID)).thenReturn(BigDecimal.ONE);
        when(certificateBusinessService.getCertificatePoints(ID)).thenReturn(BigDecimal.ONE);

        Calculation result = businessService.getById(ID);

        assertEquals(calc, result);
        assertEquals(BigDecimal.valueOf(3), result.getPoints()); // 1 + 1 + 1 = 3
        verify(persistenceService).getById(ID);
        verify(experienceBusinessService).getExperiencePoints(ID);
        verify(degreeBusinessService).getDegreePoints(ID);
    }

    @DisplayName("Should throw error when calculation with id does not exist")
    @Test
    void shouldNotGetByIdAndThrowError() {
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());
        PCTSException exception = assertThrows(PCTSException.class, () -> businessService.getById(ID));

        GenericErrorDto expectedError = new GenericErrorDto(ErrorKey.NOT_FOUND,
                Map.of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "id", FieldKey.IS, ID.toString()));
        assertEquals(List.of(expectedError), exception.getErrors());
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should create calculation with experiences")
    @Test
    void shouldCreate() {
        when(persistenceService.save(calculation)).thenReturn(calculation);

        Calculation result = businessService.create(calculation);

        assertEquals(calculation, result);

        verify(validationService).validateOnCreate(calculation);
        verify(experienceBusinessService).createExperienceCalculations(calculation);
        verify(certificateBusinessService).createCertificateCalculations(calculation);
        verify(degreeBusinessService).createDegreeCalculations(calculation);
        verify(persistenceService).save(calculation);
    }

    @DisplayName("Should update calculation with experiences")
    @Test
    void shouldUpdate() {
        when(persistenceService.getById(ID)).thenReturn(Optional.of(calculation));
        when(persistenceService.save(calculation)).thenReturn(calculation);

        Calculation result = businessService.update(ID, calculation);

        assertEquals(calculation, result);
        verify(validationService).validateOnUpdate(ID, calculation);
        verify(persistenceService).getById(ID);
        verify(persistenceService).save(calculation);
    }

    @DisplayName("Should throw exception when updating non-existing calculation")
    @Test
    void shouldThrowExceptionWhenUpdatingNotFound() {
        when(persistenceService.getById(ID)).thenReturn(Optional.empty());
        assertThrows(PCTSException.class, () -> businessService.update(ID, calculation));
        verify(persistenceService).getById(ID);
        verify(persistenceService, never()).save(any());
    }
}
