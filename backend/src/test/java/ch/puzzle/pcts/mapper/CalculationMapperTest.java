package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CalculationMapperTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long ROLE_ID = 2L;
    private static final Long CALC_ID = 3L;

    private static final CalculationState STATE = CalculationState.ACTIVE;
    private static final LocalDate PUBLICATION_DATE = LocalDate.now();
    private static final String PUBLICIZED_BY = "Ldap User";
    private static final BigDecimal POINTS = BigDecimal.valueOf(10);

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

    private CalculationMapper calculationMapper;

    @BeforeEach
    void setUp() {
        calculationMapper = new CalculationMapper(memberMapper,
                                                  memberBusinessService,
                                                  roleMapper,
                                                  roleBusinessService,
                                                  experienceCalculationMapper,
                                                  degreeCalculationMapper,
                                                  certificateCalculationMapper,
                                                  leadershipExperienceCalculationMapper);
    }

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

    private CalculationInputDto createInput() {
        return new CalculationInputDto(MEMBER_ID,
                                       STATE,
                                       ROLE_ID,
                                       List.of(1L, 2L),
                                       List.of(10L),
                                       List.of(mock(DegreeCalculationInputDto.class)),
                                       List.of(mock(ExperienceCalculationInputDto.class)));
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
        when(certificateCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(CertificateCalculationDto.class)));
        when(degreeCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(DegreeCalculationDto.class)));
        when(experienceCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(ExperienceCalculationDto.class)));

        CalculationDto result = calculationMapper.toDto(calc);

        assertNotNull(result);
        assertEquals(CALC_ID, result.id());
        assertEquals(STATE, result.state());
        assertEquals(PUBLICATION_DATE, result.publicationDate());
        assertEquals(PUBLICIZED_BY, result.publicizedBy());
        assertEquals(POINTS, result.points());
        assertEquals(expectedMemberDto, result.member());
        assertEquals(expectedRoleDto, result.role());
        assertNotNull(result.certificates());
        assertNotNull(result.degrees());
        assertNotNull(result.experiences());
        // assertNotNull(result.leadershipExperiences()); //Is not handled by the Mapper
        // yet

        verify(memberMapper).toDto(member);
        verify(roleMapper).toDto(role);
        verify(certificateCalculationMapper).toDto(anyList());
        verify(degreeCalculationMapper).toDto(anyList());
        verify(experienceCalculationMapper).toDto(anyList());
    }

    @DisplayName("Should map CalculationInputDto to Calculation")
    @Test
    void shouldReturnCalculation() {
        CalculationInputDto input = createInput();
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

    @DisplayName("Should throw when Member not found")
    @Test
    void shouldThrowWhenMemberNotFound() {
        CalculationInputDto input = createInput();

        when(memberBusinessService.getById(anyLong())).thenThrow(new RuntimeException("Member not found"));

        assertThrows(RuntimeException.class, () -> calculationMapper.fromDto(input));
    }

    @DisplayName("Should throw when Role not found")
    @Test
    void shouldThrowWhenRoleNotFound() {
        CalculationInputDto input = createInput();

        when(memberBusinessService.getById(MEMBER_ID)).thenReturn(createMember());
        when(roleBusinessService.getById(anyLong())).thenThrow(new RuntimeException("Role not found"));

        assertThrows(RuntimeException.class, () -> calculationMapper.fromDto(input));
    }
}
