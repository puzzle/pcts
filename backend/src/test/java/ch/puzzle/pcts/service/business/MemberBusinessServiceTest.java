package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.util.TestData.MEMBER_1_ID;
import static ch.puzzle.pcts.util.TestData.ROLE_2_ID;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.persistence.MemberPersistenceService;
import ch.puzzle.pcts.service.validation.MemberValidationService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberBusinessServiceTest
        extends
            BaseBusinessTest<Member, MemberPersistenceService, MemberValidationService, MemberBusinessService> {

    @Mock
    private MemberPersistenceService persistenceService;

    @Mock
    private MemberValidationService validationService;

    @Mock
    private RoleBusinessService roleBusinessService;

    @Mock
    private CalculationBusinessService calculationBusinessService;

    @InjectMocks
    @Spy
    private MemberBusinessService businessService;

    @Override
    Member getModel() {
        return Mockito.spy(MEMBER_1);
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
        when(persistenceService.getAll()).thenReturn(MEMBERS);

        List<Member> result = businessService.getAll();

        assertEquals(3, result.size());
        assertEquals(MEMBERS, result);
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
        when(businessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(roleBusinessService.getById(ROLE_2_ID)).thenReturn(ROLE_2);
        when(calculationBusinessService.getAllByMemberAndRole(MEMBER_1, ROLE_2)).thenReturn(CALCULATIONS);

        List<Calculation> result = businessService.getAllCalculationsByMemberIdAndRoleId(MEMBER_1_ID, ROLE_2_ID);

        assertEquals(4, result.size());
        assertEquals(CALCULATIONS, result);

        verify(persistenceService).getById(MEMBER_1_ID);
        verify(roleBusinessService).getById(ROLE_2_ID);
        verify(calculationBusinessService).getAllByMemberAndRole(MEMBER_1, ROLE_2);
    }

    @DisplayName("Should get calculations by memberId only when roleId is null")
    @Test
    void shouldGetCalculationsByMemberIdOnly() {
        when(businessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(calculationBusinessService.getAllByMember(MEMBER_1)).thenReturn(CALCULATIONS);
        List<Calculation> result = businessService.getAllCalculationsByMemberIdAndRoleId(MEMBER_1_ID, null);

        assertEquals(4, result.size());
        assertEquals(CALCULATIONS, result);

        verify(persistenceService).getById(MEMBER_1_ID);
        verify(calculationBusinessService).getAllByMember(MEMBER_1);
    }

    @DisplayName("Should return empty list when no calculations found")
    @Test
    void shouldReturnEmptyListWhenNoCalculationsFound() {
        Long memberId = 1L;

        when(businessService.getById(memberId)).thenReturn(MEMBER_1);
        when(calculationBusinessService.getAllByMember(MEMBER_1)).thenReturn(Collections.emptyList());

        List<Calculation> result = businessService.getAllCalculationsByMemberIdAndRoleId(memberId, null);

        assertEquals(0, result.size());
    }

    @DisplayName("Should get all active calculations by memberId")
    @Test
    void shouldGetAllActiveCalculationsByMemberId() {
        when(businessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(calculationBusinessService.getAllByMemberAndState(MEMBER_1, CalculationState.ACTIVE))
                .thenReturn(CALCULATIONS);

        List<Calculation> result =
                businessService.getAllActiveCalculationsByMemberId(MEMBER_1_ID);

        assertEquals(CALCULATIONS, result);

        verify(businessService).getById(MEMBER_1_ID);
        verify(calculationBusinessService)
                .getAllByMemberAndState(MEMBER_1, CalculationState.ACTIVE);
    }

    @DisplayName("Should return empty list when no active calculations exist")
    @Test
    void shouldReturnEmptyListWhenNoActiveCalculationsExist() {
        when(businessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(calculationBusinessService.getAllByMemberAndState(MEMBER_1, CalculationState.ACTIVE))
                .thenReturn(Collections.emptyList());

        List<Calculation> result =
                businessService.getAllActiveCalculationsByMemberId(MEMBER_1_ID);

        Assertions.assertTrue(result.isEmpty());

        verify(businessService).getById(MEMBER_1_ID);
        verify(calculationBusinessService)
                .getAllByMemberAndState(MEMBER_1, CalculationState.ACTIVE);
    }

}
