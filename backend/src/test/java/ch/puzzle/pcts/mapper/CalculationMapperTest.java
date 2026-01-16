package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class CalculationMapperTest {

    @Mock
    private MemberMapper memberMapper;
    @Mock
    private RoleMapper roleMapper;
    @Mock
    private MemberBusinessService memberBusinessService;
    @Mock
    private RoleBusinessService roleBusinessService;
    @Mock
    private ExperienceCalculationMapper experienceCalculationMapper;
    @Mock
    private DegreeCalculationMapper degreeCalculationMapper;
    @Mock
    private CertificateCalculationMapper certificateCalculationMapper;
    @Mock
    private LeadershipExperienceCalculationMapper leadershipExperienceCalculationMapper;

    @InjectMocks
    private CalculationMapper calculationMapper;

    @DisplayName("Should map Calculation to CalculationDto")
    @Test
    void shouldReturnCalculationDto() {
        when(memberMapper.toDto(MEMBER_1)).thenReturn(MEMBER_1_DTO);
        when(roleMapper.toDto(ROLE_2)).thenReturn(ROLE_2_DTO);

        when(degreeCalculationMapper.toDto(CALCULATION_1.getDegreeCalculations()))
                .thenReturn(CALCULATION_DTO_1.degreeCalculations());
        when(experienceCalculationMapper.toDto(CALCULATION_1.getExperienceCalculations()))
                .thenReturn(CALCULATION_DTO_1.experienceCalculations());
        when(certificateCalculationMapper.toDto(CALCULATION_1.getCertificateCalculationsWithCertificateType()))
                .thenReturn(CALCULATION_DTO_1.certificateCalculations());
        when(leadershipExperienceCalculationMapper.toDto(CALCULATION_1.getCertificatesCalculationsWithLeadershipExperienceType()))
                .thenReturn(CALCULATION_DTO_1.leadershipExperienceCalculations());

        CalculationDto result = calculationMapper.toDto(CALCULATION_1);

        assertNotNull(result);
        assertEquals(CALCULATION_1_ID, result.id());
        assertEquals(CALCULATION_1.getState(), result.state());
        assertEquals(CALCULATION_1.getPublicationDate(), result.publicationDate());
        assertEquals(CALCULATION_1.getPublicizedBy(), result.publicizedBy());
        assertEquals(MEMBER_1_DTO, result.member());
        assertEquals(ROLE_2_DTO, result.role());

        assertEquals(CALCULATION_DTO_1.degreeCalculations(), result.degreeCalculations());
        assertEquals(CALCULATION_DTO_1.experienceCalculations(), result.experienceCalculations());

        verify(memberMapper).toDto(MEMBER_1);
        verify(roleMapper).toDto(ROLE_2);
    }

    @DisplayName("Should map CalculationInputDto to Calculation")
    @Test
    void shouldReturnCalculation() {
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(roleBusinessService.getById(ROLE_2_ID)).thenReturn(ROLE_2);

        when(degreeCalculationMapper.fromDto(anyList())).thenReturn(Collections.emptyList());
        when(experienceCalculationMapper.fromDto(anyList())).thenReturn(Collections.emptyList());
        when(certificateCalculationMapper.fromDto(anyList())).thenReturn(Collections.emptyList());
        when(leadershipExperienceCalculationMapper.fromDto(anyList())).thenReturn(Collections.emptyList());

        Calculation result = calculationMapper.fromDto(CALCULATION_INPUT_DTO_1);

        assertNotNull(result);
        assertEquals(CALCULATION_1.getState(), result.getState());
        assertNull(result.getPublicizedBy());
        assertNull(result.getPublicationDate());

        assertEquals(MEMBER_1, result.getMember());
        assertEquals(ROLE_2, result.getRole());

        assertTrue(result.getCertificateCalculations().isEmpty());
        assertTrue(result.getExperienceCalculations().isEmpty());
        assertTrue(result.getDegreeCalculations().isEmpty());

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(roleBusinessService).getById(ROLE_2_ID);
        verify(degreeCalculationMapper).fromDto(anyList());
        verify(experienceCalculationMapper).fromDto(anyList());
        verify(certificateCalculationMapper).fromDto(anyList());
        verify(leadershipExperienceCalculationMapper).fromDto(anyList());
    }

    @DisplayName("Should map list of Calculations to CalculationDto list")
    @Test
    void shouldReturnListOfCalculationDto() {
        List<Calculation> calculations = List.of(CALCULATION_1, CALCULATION_2);

        when(memberMapper.toDto(any(Member.class))).thenReturn(MEMBER_1_DTO);
        when(roleMapper.toDto(any(Role.class))).thenReturn(ROLE_2_DTO);

        when(degreeCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(experienceCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(certificateCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(leadershipExperienceCalculationMapper.toDto(anyList())).thenReturn(List.of());

        List<CalculationDto> result = calculationMapper.toDto(calculations);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(CALCULATION_1_ID, result.get(0).id());
        assertEquals(CALCULATION_2_ID, result.get(1).id());

        verify(memberMapper, times(2)).toDto(any(Member.class));
        verify(roleMapper, times(2)).toDto(any(Role.class));
    }

    @DisplayName("Should map list of CalculationInputDto to Calculation list")
    @Test
    void shouldReturnListOfCalculation() {
        List<CalculationInputDto> inputList = List.of(CALCULATION_INPUT_DTO_1, CALCULATION_INPUT_DTO_2);

        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(memberBusinessService.getById(MEMBER_2_ID)).thenReturn(MEMBER_2);

        when(roleBusinessService.getById(ROLE_2_ID)).thenReturn(ROLE_2);

        List<Calculation> result = calculationMapper.fromDto(inputList);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(MEMBER_1, result.get(0).getMember());
        assertEquals(MEMBER_2, result.get(1).getMember());

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(memberBusinessService).getById(MEMBER_2_ID);
        verify(roleBusinessService, times(2)).getById(ROLE_2_ID);
    }

    @DisplayName("Should throw when Member not found")
    @Test
    void shouldThrowWhenMemberNotFound() {
        when(memberBusinessService.getById(MEMBER_1_ID))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of()));

        assertThrows(PCTSException.class, () -> calculationMapper.fromDto(CALCULATION_INPUT_DTO_1));

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verifyNoInteractions(roleBusinessService);
    }

    @DisplayName("Should throw when Role not found")
    @Test
    void shouldThrowWhenRoleNotFound() {
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(roleBusinessService.getById(ROLE_2_ID))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of()));

        assertThrows(PCTSException.class, () -> calculationMapper.fromDto(CALCULATION_INPUT_DTO_1));

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(roleBusinessService).getById(ROLE_2_ID);
    }
}