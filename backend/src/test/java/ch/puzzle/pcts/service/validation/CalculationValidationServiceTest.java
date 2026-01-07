package ch.puzzle.pcts.service.validation;

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

    private static final Long MEMBER_ID = 1L;
    private static final Long ROLE_ID = 1L;
    private static final Long CALCULATION_ID = 1L;

    @Mock
    private CalculationPersistenceService persistenceService;

    @InjectMocks
    private CalculationValidationService service;

    @Override
    Calculation getValidModel() {

        Member member = new Member();
        member.setId(MEMBER_ID);

        Role role = new Role();
        role.setId(ROLE_ID);

        Calculation calculation = new Calculation();
        calculation.setMember(member);
        calculation.setRole(role);
        calculation.setState(CalculationState.ACTIVE);
        calculation.setPublicationDate(LocalDate.now());
        calculation.setPublicizedBy("Admin");

        return calculation;
    }

    @Override
    CalculationValidationService getService() {
        return service;
    }

    private static Calculation createCalculation(Member member, Role role, CalculationState state) {
        Calculation calculation = new Calculation();
        calculation.setMember(member);
        calculation.setRole(role);
        calculation.setState(state);
        calculation.setPublicationDate(LocalDate.now());
        calculation.setPublicizedBy("Admin");
        return calculation;
    }

    static Stream<Arguments> invalidModelProvider() {
        Member validMember = new Member();
        validMember.setId(MEMBER_ID);

        Role validRole = new Role();
        validRole.setId(ROLE_ID);

        return Stream
                .of(Arguments
                        .of(createCalculation(null, validRole, CalculationState.ACTIVE),
                            List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "member"))),
                    Arguments
                            .of(createCalculation(validMember, null, CalculationState.ACTIVE),
                                List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "role"))),
                    Arguments
                            .of(createCalculation(validMember, validRole, null),
                                List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "state"))));
    }

    @DisplayName("Should call correct validate method on validateOnCreate()")
    @Test
    void shouldCallCorrectMethodOnValidateOnCreate() {
        Calculation calculation = getValidModel();

        CalculationValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Calculation>) spyService).validateOnCreate(calculation);

        spyService.validateOnCreate(calculation);

        verify(spyService).validateOnCreate(calculation);
        verifyNoMoreInteractions(persistenceService);
    }

    @DisplayName("Should call correct validate method on validateOnUpdate()")
    @Test
    void shouldCallCorrectMethodOnValidateOnUpdate() {
        Calculation calculation = getValidModel();

        CalculationValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Calculation>) spyService).validateOnUpdate(CALCULATION_ID, calculation);

        spyService.validateOnUpdate(CALCULATION_ID, calculation);

        verify(spyService).validateOnUpdate(CALCULATION_ID, calculation);
        verifyNoMoreInteractions(persistenceService);
    }
}
