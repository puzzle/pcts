package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
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

}
