package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.util.TestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationValidationServiceTest extends ValidationBaseServiceTest<Calculation, CalculationValidationService> {

    @Mock
    private CalculationPersistenceService persistenceService;

    @InjectMocks
    private CalculationValidationService service;

    @Override
    Calculation getValidModel() {

        Member member = new Member();
        member.setId(1L);

        Role role = new Role();
        role.setId(1L);

        Calculation c = new Calculation();
        c.setMember(member);
        c.setRole(role);
        c.setState(CalculationState.ACTIVE);
        c.setPublicationDate(LocalDate.now());
        c.setPublicizedBy("Admin");

        return c;
    }

    @Override
    CalculationValidationService getService() {
        return service;
    }

    private static Calculation createCalculation(Member member, Role role, CalculationState state) {
        Calculation c = new Calculation();
        c.setMember(member);
        c.setRole(role);
        c.setState(state);
        c.setPublicationDate(DATE_NOW);
        c.setPublicizedBy("Admin");
        return c;
    }

    static Stream<Arguments> invalidModelProvider() {
        return Stream
                .of(Arguments
                        .of(createCalculation(null, ROLE_2, CalculationState.ACTIVE),
                            List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "member"))),
                    Arguments
                            .of(createCalculation(MEMBER_1, null, CalculationState.ACTIVE),
                                List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "role"))),
                    Arguments
                            .of(createCalculation(MEMBER_1, ROLE_2, null),
                                List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "state"))));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallCorrectMethodOnValidateOnCreate() {
        Calculation calculation = getValidModel();

        CalculationValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Calculation>) spyService).validateOnCreate(any());

        spyService.validateOnCreate(calculation);

        verify(spyService).validateOnCreate(calculation);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallCorrectMethodOnValidateOnUpdate() {
        Calculation calculation = getValidModel();

        CalculationValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Calculation>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(CALCULATION_1_ID, calculation);

        verify(spyService).validateOnUpdate(CALCULATION_1_ID, calculation);
        verifyNoMoreInteractions(persistenceService);
    }
}
