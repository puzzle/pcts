package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.util.TestData.MEMBER_1_ID;
import static ch.puzzle.pcts.util.TestData.ROLE_1_ID;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CalculationTemplateBusinessServiceTest {

    @Mock
    private MemberBusinessService memberBusinessService;
    @Mock
    private DegreeBusinessService degreeBusinessService;
    @Mock
    private CertificateBusinessService certificateBusinessService;
    @Mock
    private LeadershipExperienceBusinessService leadershipExperienceBusinessService;
    @Mock
    private ExperienceBusinessService experienceBusinessService;
    @Mock
    private RoleBusinessService roleBusinessService;

    @InjectMocks
    private CalculationTemplateBusinessService calculationTemplateBusinessService;

    @DisplayName("Should get Calculation Template by Role and Member ID")
    @Test
    void shouldGetTemplate() {
        Long memberId = MEMBER_1_ID;
        Long roleId = ROLE_1_ID;
        Member member = MEMBER_1;
        Role role = ROLE_1;

        when(memberBusinessService.getById(memberId)).thenReturn(member);
        when(roleBusinessService.getById(roleId)).thenReturn(role);

        when(degreeBusinessService.findAllByMember(member)).thenReturn(List.of(DEGREE_1));
        when(experienceBusinessService.findAllByMember(member)).thenReturn(List.of(EXPERIENCE_2));
        when(leadershipExperienceBusinessService.findAllByMember(member)).thenReturn(List.of(LEADERSHIP_EXPERIENCE_1));
        when(certificateBusinessService.findAllByMember(member)).thenReturn(List.of(CERTIFICATE_1));

        Calculation result = calculationTemplateBusinessService.getCalculationTemplate(memberId, roleId);

        assertNotNull(result);
        assertEquals(member, result.getMember());
        assertEquals(role, result.getRole());
        assertEquals(CalculationState.DRAFT, result.getState());

        assertEquals(1, result.getDegreeCalculations().size());
        assertEquals(1, result.getExperienceCalculations().size());
        assertEquals(1, result.getLeadershipExperienceCalculations().size());
        assertEquals(1, result.getCertificateCalculations().size());

        verify(memberBusinessService).getById(memberId);
        verify(roleBusinessService).getById(roleId);
        verify(degreeBusinessService).findAllByMember(member);
        verify(experienceBusinessService).findAllByMember(member);
        verify(leadershipExperienceBusinessService).findAllByMember(member);
        verify(certificateBusinessService).findAllByMember(member);
    }
}