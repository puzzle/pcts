package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ch.puzzle.pcts.Constants.CALCULATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculationValidationServiceTest
        extends ValidationBaseServiceTest<Calculation, CalculationValidationService> {

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
        c.setPublicationDate(LocalDate.now());
        c.setPublicizedBy("Admin");
        return c;
    }

    static Stream<Arguments> invalidModelProvider() {
        Member validMember = new Member();
        validMember.setId(1L);

        Role validRole = new Role();
        validRole.setId(1L);

        return Stream.of(
                Arguments.of(
                        createCalculation(null, validRole, CalculationState.ACTIVE),
                        List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "member"))
                ),
                Arguments.of(
                        createCalculation(validMember, null, CalculationState.ACTIVE),
                        List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "role"))
                ),
                Arguments.of(
                        createCalculation(validMember, validRole, null),
                        List.of(Map.of(FieldKey.CLASS, "Calculation", FieldKey.FIELD, "state"))
                )
        );
    }

    @DisplayName("Should throw exception on validateUserOnlyHasOneActiveCalculationPerRole() when creating and active calculation exists")
    @Test
    void shouldThrowExceptionOnCreateWhenActiveCalculationExists() {
        Calculation calculation = getValidModel();

        Calculation existingActive = getValidModel();
        existingActive.setId(10L);
        existingActive.setState(CalculationState.ACTIVE);

        when(persistenceService.getAllByMemberIdAndRoleIdAndState(
                calculation.getMember().getId(),
                calculation.getRole().getId(),
                CalculationState.ACTIVE
        )).thenReturn(List.of(existingActive));

        PCTSException exception = assertThrows(
                PCTSException.class,
                () -> service.validateUserOnlyHasOneActiveCalculationPerRole(calculation, null)
        );

        assertEquals(List.of(ErrorKey.INVALID_ARGUMENT), exception.getErrorKeys());
        assertEquals(
                List.of(
                        Map.of(
                                FieldKey.ENTITY, CALCULATION,
                                FieldKey.FIELD, "member",
                                FieldKey.IS, CalculationState.ACTIVE.toString()
                        )
                ),
                exception.getErrorAttributes()
        );
    }


    @DisplayName("Should throw exception on validateUserOnlyHasOneActiveCalculationPerRole() when updating and another active calculation exists with the same id")
    @Test
    void shouldThrowExceptionOnUpdateWhenActiveCalculationExistsForSameId() {
        Calculation calculation = getValidModel();
        calculation.setId(5L);

        Calculation existingActive = getValidModel();
        existingActive.setId(5L);
        existingActive.setState(CalculationState.ACTIVE);

        when(persistenceService.getAllByMemberIdAndRoleIdAndState(
                calculation.getMember().getId(),
                calculation.getRole().getId(),
                CalculationState.ACTIVE
        )).thenReturn(List.of(existingActive));

        PCTSException exception = assertThrows(
                PCTSException.class,
                () -> service.validateUserOnlyHasOneActiveCalculationPerRole(calculation, 5L)
        );

        assertEquals(List.of(ErrorKey.INVALID_ARGUMENT), exception.getErrorKeys());
        assertEquals(
                List.of(
                        Map.of(
                                FieldKey.ENTITY, CALCULATION,
                                FieldKey.FIELD, "member",
                                FieldKey.IS, CalculationState.ACTIVE.toString()
                        )
                ),
                exception.getErrorAttributes()
        );
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
        Long id = 1L;
        Calculation calculation = getValidModel();

        CalculationValidationService spyService = spy(service);
        doNothing().when((ValidationBase<Calculation>) spyService).validateOnUpdate(anyLong(), any());

        spyService.validateOnUpdate(id, calculation);

        verify(spyService).validateOnUpdate(id, calculation);
        verifyNoMoreInteractions(persistenceService);
    }
}
