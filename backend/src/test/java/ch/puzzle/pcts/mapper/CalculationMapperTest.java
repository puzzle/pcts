package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    private Member createMember() {
        Member m = new Member();
        m.setId(MEMBER_ID);
        return m;
    }

    private Role createRole() {
        Role r = new Role();
        r.setId(ROLE_ID);
        return r;
    }

    private Calculation createCalculation(Member member, Role role) {
        return new Calculation(CALC_ID, member, role, STATE, PUBLICATION_DATE, PUBLICIZED_BY);
    }

    private CalculationInputDto createInput() {
        return new CalculationInputDto(MEMBER_ID, ROLE_ID, STATE);
    }

    @DisplayName("Should map Calculation to CalculationDto")
    @Test
    void shouldReturnCalculationDto() {
        Member member = createMember();
        Role role = createRole();
        Calculation calc = createCalculation(member, role);

        MemberDto expectedMemberDto = mock(MemberDto.class);
        RoleDto expectedRoleDto = mock(RoleDto.class);

        when(memberMapper.toDto(member)).thenReturn(expectedMemberDto);
        when(roleMapper.toDto(role)).thenReturn(expectedRoleDto);

        CalculationDto result = calculationMapper.toDto(calc);

        assertNotNull(result);
        assertEquals(CALC_ID, result.id());
        assertEquals(STATE, result.state());
        assertEquals(PUBLICATION_DATE, result.publicationDate());
        assertEquals(PUBLICIZED_BY, result.publicizedBy());
        assertEquals(expectedMemberDto, result.member());
        assertEquals(expectedRoleDto, result.role());

        verify(memberMapper).toDto(member);
        verify(roleMapper).toDto(role);
    }

    @DisplayName("Should map CalculationInputDto to Calculation")
    @Test
    void shouldReturnCalculation() {
        CalculationInputDto input = createInput();
        Member member = createMember();
        Role role = createRole();

        when(memberBusinessService.getById(MEMBER_ID)).thenReturn(member);
        when(roleBusinessService.getById(ROLE_ID)).thenReturn(role);

        Calculation result = calculationMapper.fromDto(input);

        assertNotNull(result);
        assertEquals(STATE, result.getState());
        assertNull(result.getPublicizedBy());
        assertNull(result.getPublicationDate());
        assertEquals(MEMBER_1, result.getMember());
        assertEquals(ROLE_2, result.getRole());
        assertEquals(0, result.getCertificateCalculations().size());
        assertEquals(0, result.getExperienceCalculations().size());
        assertEquals(0, result.getDegreeCalculations().size());

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
        Calculation c1 = new Calculation(1L, new Member(), new Role(), STATE, null, null);
        Calculation c2 = new Calculation(2L, new Member(), new Role(), STATE, null, null);

        when(memberMapper.toDto(any(Member.class))).thenReturn(mock(MemberDto.class));
        when(roleMapper.toDto(any(Role.class))).thenReturn(mock(RoleDto.class));
        when(degreeCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(experienceCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(certificateCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(leadershipExperienceCalculationMapper.toDto(anyList())).thenReturn(List.of());

        List<CalculationDto> result = calculationMapper.toDto(List.of(c1, c2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        verify(memberMapper, times(2)).toDto(any(Member.class));
        verify(roleMapper, times(2)).toDto(any(Role.class));
        verify(degreeCalculationMapper, times(2)).toDto(anyList());
        verify(experienceCalculationMapper, times(2)).toDto(anyList());
        verify(certificateCalculationMapper, times(2)).toDto(anyList());
        verify(leadershipExperienceCalculationMapper, times(2)).toDto(anyList());
    }

    @DisplayName("Should map list of CalculationInputDto to Calculation list")
    @Test
    void shouldReturnListOfCalculation() {
        CalculationInputDto i1 = new CalculationInputDto(1L, 10L, STATE);
        CalculationInputDto i2 = new CalculationInputDto(2L, 20L, STATE);

        when(memberBusinessService.getById(1L)).thenReturn(new Member());
        when(memberBusinessService.getById(2L)).thenReturn(new Member());
        when(roleBusinessService.getById(10L)).thenReturn(new Role());
        when(roleBusinessService.getById(20L)).thenReturn(new Role());

        List<Calculation> result = calculationMapper.fromDto(List.of(i1, i2));

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(memberBusinessService).getById(1L);
        verify(memberBusinessService).getById(2L);
        verify(roleBusinessService).getById(10L);
        verify(roleBusinessService).getById(20L);
    }

    @DisplayName("Should return CertificateCalculations depending on kind")
    @Test
    void shouldThrowWhenMemberNotFound() {
        CalculationInputDto input = createInput();

        when(memberBusinessService.getById(anyLong())).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of()));

        assertThrows(PCTSException.class, () -> calculationMapper.fromDto(input));
    }

    @DisplayName("Should throw when Role not found")
    @Test
    void shouldThrowWhenRoleNotFound() {
        CalculationInputDto input = createInput();

        when(memberBusinessService.getById(MEMBER_ID)).thenReturn(createMember());
        when(roleBusinessService.getById(anyLong())).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of()));

        assertThrows(PCTSException.class, () -> calculationMapper.fromDto(input));
    }
}
