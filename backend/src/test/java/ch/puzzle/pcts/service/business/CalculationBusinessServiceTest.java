package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.util.TestData.CALCULATION_1_ID;
import static ch.puzzle.pcts.util.TestData.CALCULATION_2_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import java.math.BigDecimal;
import java.util.List;
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
        calc.setId(CALCULATION_1_ID);

        when(persistenceService.getById(CALCULATION_1_ID)).thenReturn(calc);
        when(experienceBusinessService.getExperiencePoints(CALCULATION_1_ID)).thenReturn(BigDecimal.ONE);
        when(degreeBusinessService.getDegreePoints(CALCULATION_1_ID)).thenReturn(BigDecimal.ONE);
        when(certificateBusinessService.getCertificatePoints(CALCULATION_1_ID)).thenReturn(BigDecimal.ONE);

        Calculation result = businessService.getById(CALCULATION_1_ID);

        assertEquals(calc, result);
        assertEquals(BigDecimal.valueOf(3), result.getPoints());
        verify(persistenceService).getById(CALCULATION_1_ID);
        verify(experienceBusinessService).getExperiencePoints(CALCULATION_1_ID);
        verify(degreeBusinessService).getDegreePoints(CALCULATION_1_ID);
        verify(certificateBusinessService).getCertificatePoints(CALCULATION_1_ID);

    }

    @Override
    @DisplayName("Should create calculation with experiences")
    @Test
    void shouldCreate() {
        when(experienceBusinessService.getExperiencePoints(anyLong())).thenReturn(BigDecimal.ONE);
        when(degreeBusinessService.getDegreePoints(anyLong())).thenReturn(BigDecimal.ONE);
        when(certificateBusinessService.getCertificatePoints(anyLong())).thenReturn(BigDecimal.ONE);
        super.shouldCreate();
        verify(experienceBusinessService).createExperienceCalculations(calculation);
        verify(certificateBusinessService).createCertificateCalculations(calculation);
        verify(degreeBusinessService).createDegreeCalculations(calculation);
        verify(experienceBusinessService).getExperiencePoints(anyLong());
        verify(degreeBusinessService).getDegreePoints(anyLong());
        verify(certificateBusinessService).getCertificatePoints(anyLong());
    }

    @Override
    @DisplayName("Should update calculation with experiences")
    @Test
    void shouldUpdate() {
        when(experienceBusinessService.getExperiencePoints(anyLong())).thenReturn(BigDecimal.ONE);
        when(degreeBusinessService.getDegreePoints(anyLong())).thenReturn(BigDecimal.ONE);
        when(certificateBusinessService.getCertificatePoints(anyLong())).thenReturn(BigDecimal.ONE);
        super.shouldUpdate();
        verify(validationService).validateOnUpdate(CALCULATION_1_ID, calculation);
        verify(experienceBusinessService).updateExperienceCalculations(calculation);
        verify(certificateBusinessService).updateCertificateCalculations(calculation);
        verify(degreeBusinessService).updateDegreeCalculations(calculation);
        verify(experienceBusinessService).getExperiencePoints(anyLong());
        verify(degreeBusinessService).getDegreePoints(anyLong());
        verify(certificateBusinessService).getCertificatePoints(anyLong());
    }

    @DisplayName("Should get all calculations by member and set points")
    @Test
    void shouldGetAllByMember() {
        Member member = mock(Member.class);

        Calculation calc1 = new Calculation();
        calc1.setId(CALCULATION_1_ID);
        Calculation calc2 = new Calculation();
        calc2.setId(CALCULATION_2_ID);
        List<Calculation> calculations = List.of(calc1, calc2);

        when(persistenceService.getAllByMember(member)).thenReturn(calculations);
        when(experienceBusinessService.getExperiencePoints(calc1.getId())).thenReturn(BigDecimal.ONE);
        when(degreeBusinessService.getDegreePoints(calc1.getId())).thenReturn(BigDecimal.ONE);
        when(certificateBusinessService.getCertificatePoints(calc1.getId())).thenReturn(BigDecimal.ONE);

        when(experienceBusinessService.getExperiencePoints(calc2.getId())).thenReturn(BigDecimal.TEN);
        when(degreeBusinessService.getDegreePoints(calc2.getId())).thenReturn(BigDecimal.ONE);
        when(certificateBusinessService.getCertificatePoints(calc2.getId())).thenReturn(BigDecimal.ZERO);

        List<Calculation> result = businessService.getAllByMember(member);

        assertEquals(2, result.size());
        assertEquals(BigDecimal.valueOf(3), result.get(0).getPoints());
        assertEquals(BigDecimal.valueOf(11), result.get(1).getPoints());

        verify(persistenceService).getAllByMember(member);
    }

    @DisplayName("Should get all calculations by member and role and set points")
    @Test
    void shouldGetAllByMemberAndRole() {
        Member member = mock(Member.class);
        Role role = mock(Role.class);

        Calculation calc1 = new Calculation();
        List<Calculation> calculations = List.of(calc1);

        when(persistenceService.getAllByMemberAndRole(member, role)).thenReturn(calculations);
        when(experienceBusinessService.getExperiencePoints(calc1.getId())).thenReturn(BigDecimal.ONE);
        when(degreeBusinessService.getDegreePoints(calc1.getId())).thenReturn(BigDecimal.ZERO);
        when(certificateBusinessService.getCertificatePoints(calc1.getId())).thenReturn(BigDecimal.ONE);

        List<Calculation> result = businessService.getAllByMemberAndRole(member, role);

        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(2), result.getFirst().getPoints());

        verify(persistenceService).getAllByMemberAndRole(member, role);
    }
}
