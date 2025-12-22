package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationMapperTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long ROLE_ID = 2L;
    private static final Long CALC_ID = 3L;
    private static final Long CERTIFICATE1_ID = 1L;
    private static final Long CERTIFICATE2_ID = 2L;
    private static final Long LEADERSHIP_EXPERIENCE_ID = 10L;
    private static final CalculationState STATE = CalculationState.ACTIVE;
    private static final LocalDate PUBLICATION_DATE = LocalDate.now();
    private static final String PUBLICIZED_BY = "Ldap User";
    private static final BigDecimal POINTS = BigDecimal.TEN;

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

    // ---------------- Helper Methods ----------------

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
        Calculation calculation = Calculation.Builder
                .builder()
                .withId(CALC_ID)
                .withMember(member)
                .withRole(role)
                .withState(STATE)
                .withPublicationDate(PUBLICATION_DATE)
                .withPublicizedBy(PUBLICIZED_BY)
                .withDegrees(List.of())
                .withExperiences(List.of())
                .withCertificates(List.of())
                .build();
        calculation.setPoints(POINTS);
        return calculation;
    }

    private CalculationInputDto createCalculationInput() {
        DegreeCalculationInputDto degreeInput = mock(DegreeCalculationInputDto.class);
        ExperienceCalculationInputDto experienceInput = mock(ExperienceCalculationInputDto.class);
        return new CalculationInputDto(MEMBER_ID,
                                       STATE,
                                       ROLE_ID,
                                       List.of(CERTIFICATE1_ID, CERTIFICATE2_ID),
                                       List.of(LEADERSHIP_EXPERIENCE_ID),
                                       List.of(degreeInput),
                                       List.of(experienceInput));
    }

    private MemberDto mockMemberDto() {
        return mock(MemberDto.class);
    }
    private RoleDto mockRoleDto() {
        return mock(RoleDto.class);
    }

    // ---------------- Tests ----------------

    @DisplayName("Should map Calculation to CalculationDto")
    @Test
    void shouldReturnCalculationDto() {
        Member member = createMember();
        Role role = createRole();
        Calculation calc = createCalculation(member, role);

        MemberDto memberDto = mockMemberDto();
        RoleDto roleDto = mockRoleDto();

        when(memberMapper.toDto(member)).thenReturn(memberDto);
        when(roleMapper.toDto(role)).thenReturn(roleDto);
        when(certificateCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(CertificateCalculationDto.class)));
        when(leadershipExperienceCalculationMapper.toDto(anyList()))
                .thenReturn(List.of(mock(LeadershipExperienceCalculationDto.class)));
        when(degreeCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(DegreeCalculationDto.class)));
        when(experienceCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(ExperienceCalculationDto.class)));

        CalculationDto result = calculationMapper.toDto(calc);

        assertNotNull(result);
        assertEquals(CALC_ID, result.id());
        assertEquals(STATE, result.state());
        assertEquals(PUBLICATION_DATE, result.publicationDate());
        assertEquals(PUBLICIZED_BY, result.publicizedBy());
        assertEquals(POINTS, result.points());
        assertEquals(memberDto, result.member());
        assertEquals(roleDto, result.role());
        assertNotNull(result.certificates());
        assertNotNull(result.degrees());
        assertNotNull(result.experiences());
        assertNotNull(result.leadershipExperiences());

        verify(memberMapper).toDto(member);
        verify(roleMapper).toDto(role);
        verify(certificateCalculationMapper).toDto(anyList());
        verify(leadershipExperienceCalculationMapper).toDto(anyList());
        verify(degreeCalculationMapper).toDto(anyList());
        verify(experienceCalculationMapper).toDto(anyList());
    }

    @DisplayName("Should map CalculationInputDto to Calculation")
    @Test
    void shouldReturnCalculation() {
        CalculationInputDto input = createCalculationInput();
        Member member = createMember();
        Role role = createRole();

        when(memberBusinessService.getById(MEMBER_ID)).thenReturn(member);
        when(roleBusinessService.getById(ROLE_ID)).thenReturn(role);
        when(degreeCalculationMapper.fromDto(anyList())).thenReturn(List.of());
        when(experienceCalculationMapper.fromDto(anyList())).thenReturn(List.of());
        when(certificateCalculationMapper.fromDto(anyList())).thenReturn(List.of());

        Calculation result = calculationMapper.fromDto(input);

        assertNotNull(result);
        assertEquals(STATE, result.getState());
        assertNull(result.getPublicizedBy());
        assertNull(result.getPublicationDate());
        assertEquals(member, result.getMember());
        assertEquals(role, result.getRole());
        assertNotNull(result.getDegrees());
        assertNotNull(result.getExperiences());
        assertNotNull(result.getCertificates());

        verify(memberBusinessService).getById(MEMBER_ID);
        verify(roleBusinessService).getById(ROLE_ID);
        verify(degreeCalculationMapper).fromDto(anyList());
        verify(experienceCalculationMapper).fromDto(anyList());
        verify(certificateCalculationMapper, times(2)).fromDto(anyList());
    }

    @DisplayName("Should map list of Calculations to CalculationDto list")
    @Test
    void shouldReturnListOfCalculationDto() {
        Calculation c1 = createCalculation(new Member(), new Role());
        Calculation c2 = createCalculation(new Member(), new Role());

        when(memberMapper.toDto(any(Member.class))).thenReturn(mock(MemberDto.class));
        when(roleMapper.toDto(any(Role.class))).thenReturn(mock(RoleDto.class));
        when(degreeCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(experienceCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(certificateCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(leadershipExperienceCalculationMapper.toDto(anyList())).thenReturn(List.of());

        List<CalculationDto> result = calculationMapper.toDto(List.of(c1, c2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(CALC_ID, result.get(0).id());
        assertEquals(CALC_ID, result.get(1).id());

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
        CalculationInputDto i1 = createCalculationInput();
        CalculationInputDto i2 = createCalculationInput();

        when(memberBusinessService.getById(any(Long.class))).thenReturn(mock(Member.class));
        when(roleBusinessService.getById(any(Long.class))).thenReturn(mock(Role.class));
        when(degreeCalculationMapper.fromDto(anyList())).thenReturn(List.of());
        when(experienceCalculationMapper.fromDto(anyList())).thenReturn(List.of());
        when(certificateCalculationMapper.fromDto(anyList())).thenReturn(List.of());

        List<Calculation> result = calculationMapper.fromDto(List.of(i1, i2));

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(memberBusinessService, times(2)).getById(MEMBER_ID);
        verify(roleBusinessService, times(2)).getById(ROLE_ID);
        verify(degreeCalculationMapper, times(2)).fromDto(anyList());
        verify(experienceCalculationMapper, times(2)).fromDto(anyList());
        verify(certificateCalculationMapper, times(4)).fromDto(anyList());
    }
}
