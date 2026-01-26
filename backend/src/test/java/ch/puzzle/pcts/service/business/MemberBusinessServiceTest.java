package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.RolePointDto;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberBusinessServiceTest
        extends
            BaseBusinessTest<Member, MemberPersistenceService, MemberValidationService, MemberBusinessService> {

    @Mock
    private Member member;

    @Mock
    private List<Member> members;

    @Mock
    private MemberPersistenceService persistenceService;

    @Mock
    private MemberValidationService validationService;

    @Mock
    private RoleBusinessService roleBusinessService;

    @Mock
    private CalculationBusinessService calculationBusinessService;

    @Mock
    private List<Calculation> calculations;

    @Mock
    private Role role;

    @InjectMocks
    private MemberBusinessService businessService;

    @Override
    Member getModel() {
        return member;
    }

    @Override
    MemberPersistenceService getPersistenceService() {
        return persistenceService;
    }

    @Override
    MemberValidationService getValidationService() {
        return validationService;
    }

    @Override
    MemberBusinessService getBusinessService() {
        return businessService;
    }

    @DisplayName("Should get all")
    @Test
    void shouldGetAll() {
        when(persistenceService.getAll()).thenReturn(members);
        when(members.size()).thenReturn(2);

        List<Member> result = businessService.getAll();

        assertEquals(2, result.size());
        assertEquals(members, result);
        verify(persistenceService).getAll();
    }

    @DisplayName("Should get empty list")
    @Test
    void shouldGetEmptyList() {
        when(persistenceService.getAll()).thenReturn(Collections.emptyList());

        List<Member> result = businessService.getAll();

        assertEquals(0, result.size());
    }

    @DisplayName("Should get calculations by memberId and roleId")
    @Test
    void shouldGetCalculationsByMemberIdAndRoleId() {
        Long memberId = 1L;
        Long roleId = 2L;

        when(businessService.getById(memberId)).thenReturn(member);
        when(roleBusinessService.getById(roleId)).thenReturn(role);
        when(calculationBusinessService.getAllByMemberAndRole(member, role)).thenReturn(calculations);
        when(calculations.size()).thenReturn(3);

        List<Calculation> result = businessService.getAllCalculationsByMemberIdAndRoleId(memberId, roleId);

        assertEquals(3, result.size());
        assertEquals(calculations, result);

        verify(persistenceService).getById(memberId);
        verify(roleBusinessService).getById(roleId);
        verify(calculationBusinessService).getAllByMemberAndRole(member, role);
    }

    @DisplayName("Should get calculations by memberId only when roleId is null")
    @Test
    void shouldGetCalculationsByMemberIdOnly() {
        Long memberId = 1L;

        when(businessService.getById(memberId)).thenReturn(member);
        when(calculationBusinessService.getAllByMember(member)).thenReturn(calculations);
        when(calculations.size()).thenReturn(1);

        List<Calculation> result = businessService.getAllCalculationsByMemberIdAndRoleId(memberId, null);

        assertEquals(1, result.size());
        assertEquals(calculations, result);

        verify(persistenceService).getById(memberId);
        verify(calculationBusinessService).getAllByMember(member);
    }

    @DisplayName("Should return empty list when no calculations found")
    @Test
    void shouldReturnEmptyListWhenNoCalculationsFound() {
        Long memberId = 1L;

        when(businessService.getById(memberId)).thenReturn(member);
        when(calculationBusinessService.getAllByMember(member)).thenReturn(Collections.emptyList());

        List<Calculation> result = businessService.getAllCalculationsByMemberIdAndRoleId(memberId, null);

        assertEquals(0, result.size());
    }

    @DisplayName("Should get all role points by memberId")
    @Test
    void shouldGetAllRolePointsByMemberId() {
        Long memberId = 1L;

        Calculation calc1 = mock(Calculation.class);
        Calculation calc2 = mock(Calculation.class);

        List<Calculation> calculationList = List.of(calc1, calc2);

        when(businessService.getById(memberId)).thenReturn(member);
        when(calculationBusinessService.getAllByMemberAndState(member, CalculationState.ACTIVE))
                .thenReturn(calculationList);

        when(calc1.getRole()).thenReturn(role);
        when(calc1.getPoints()).thenReturn(BigDecimal.TEN);

        Role anotherRole = mock(Role.class);
        when(calc2.getRole()).thenReturn(anotherRole);
        when(calc2.getPoints()).thenReturn(BigDecimal.TWO);

        List<RolePointDto> result = businessService.getAllRolePointsByMemberId(memberId);

        assertEquals(2, result.size());
        assertEquals(role, result.get(0).role());
        assertEquals(BigDecimal.TEN, result.get(0).points());
        assertEquals(anotherRole, result.get(1).role());
        assertEquals(BigDecimal.TWO, result.get(1).points());

        verify(persistenceService).getById(memberId);
        verify(calculationBusinessService).getAllByMemberAndState(member, CalculationState.ACTIVE);
    }

    @DisplayName("Should return empty role points list when no active calculations exist")
    @Test
    void shouldReturnEmptyRolePointsList() {
        Long memberId = 1L;

        when(businessService.getById(memberId)).thenReturn(member);
        when(calculationBusinessService.getAllByMemberAndState(member, CalculationState.ACTIVE))
                .thenReturn(Collections.emptyList());

        List<RolePointDto> result = businessService.getAllRolePointsByMemberId(memberId);

        assertEquals(0, result.size());

        verify(persistenceService).getById(memberId);
        verify(calculationBusinessService).getAllByMemberAndState(member, CalculationState.ACTIVE);
    }

}
