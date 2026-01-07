package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationInputDto;
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

    private static final Long MEMBER_ID = 1L;
    private static final Long ROLE_ID = 2L;
    private static final Long CALC_ID = 3L;
    private static final Long CALC_2_ID = 4L;
    private static final Long CERTIFICATE1_ID = 1L;
    private static final Long CERTIFICATE2_ID = 2L;
    private static final Long LEADERSHIP_EXPERIENCE_ID = 10L;
    private static final CalculationState STATE_ACTIVE = CalculationState.ACTIVE;
    private static final CalculationState STATE_DRAFT = CalculationState.DRAFT;
    private static final LocalDate PUBLICATION_DATE = LocalDate.now();
    private static final String PUBLICIZED_BY_USER = "Ldap User";
    private static final Calculation calculation1 = createCalculation(CALC_ID,
                                                                      STATE_ACTIVE,
                                                                      PUBLICATION_DATE,
                                                                      PUBLICIZED_BY_USER,
                                                                      BigDecimal.TEN);
    private static final Calculation calculation2 = createCalculation(CALC_2_ID,
                                                                      STATE_DRAFT,
                                                                      null,
                                                                      null,
                                                                      BigDecimal.TWO);
    private static final CalculationInputDto calculationInputDto1 = createCalculationInput(CalculationState.ACTIVE);
    private static final CalculationInputDto calculationInputDto2 = createCalculationInput(CalculationState.DRAFT);

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

    private static Calculation createCalculation(Long id, CalculationState state, LocalDate publicationDate,
                                                 String publicizedBy, BigDecimal points) {
        return Calculation.Builder
                .builder()
                .withId(id)
                .withMember(mock(Member.class))
                .withRole(mock(Role.class))
                .withState(state)
                .withPublicationDate(publicationDate)
                .withPublicizedBy(publicizedBy)
                .withDegrees(List.of())
                .withExperiences(List.of())
                .withCertificates(List.of())
                .withPoints(points)
                .build();
    }

    private static CalculationInputDto createCalculationInput(CalculationState state) {
        CertificateCalculationInputDto certificateInput = mock(CertificateCalculationInputDto.class);
        LeadershipExperienceCalculationInputDto leadershipExperienceInput = mock(LeadershipExperienceCalculationInputDto.class);
        DegreeCalculationInputDto degreeInput = mock(DegreeCalculationInputDto.class);
        ExperienceCalculationInputDto experienceInput = mock(ExperienceCalculationInputDto.class);
        return new CalculationInputDto(MEMBER_ID,
                                       state,
                                       ROLE_ID,
                                       List.of(certificateInput),
                                       List.of(leadershipExperienceInput),
                                       List.of(degreeInput),
                                       List.of(experienceInput));
    }

    private MemberDto mockMemberDto() {
        return mock(MemberDto.class);
    }
    private RoleDto mockRoleDto() {
        return mock(RoleDto.class);
    }

    static Stream<Arguments> calculationProvider() {

        return Stream.of(Arguments.of(calculation1), Arguments.of(calculation2));
    }

    static Stream<Arguments> calculationInputDtoProvider() {

        return Stream.of(Arguments.of(calculationInputDto1), Arguments.of(calculationInputDto2));
    }

    @ParameterizedTest
    @MethodSource("calculationProvider")
    @DisplayName("Should map Calculation to CalculationDto")
    void shouldReturnCalculationDto(Calculation calculation) {
        MemberDto memberDto = mockMemberDto();
        RoleDto roleDto = mockRoleDto();

        when(memberMapper.toDto(calculation.getMember())).thenReturn(memberDto);
        when(roleMapper.toDto(calculation.getRole())).thenReturn(roleDto);
        when(certificateCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(CertificateCalculationDto.class)));
        when(leadershipExperienceCalculationMapper.toDto(anyList()))
                .thenReturn(List.of(mock(LeadershipExperienceCalculationDto.class)));
        when(degreeCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(DegreeCalculationDto.class)));
        when(experienceCalculationMapper.toDto(anyList())).thenReturn(List.of(mock(ExperienceCalculationDto.class)));

        CalculationDto result = calculationMapper.toDto(calculation);

        assertNotNull(result);
        assertEquals(calculation.getId(), result.id());
        assertEquals(calculation.getState(), result.state());
        assertEquals(calculation.getPublicationDate(), result.publicationDate());
        assertEquals(calculation.getPublicizedBy(), result.publicizedBy());
        assertEquals(calculation.getPoints(), result.points());
        assertEquals(memberDto, result.member());
        assertEquals(roleDto, result.role());
        assertEquals(1, result.certificates().size());
        assertEquals(1, result.experiences().size());
        assertEquals(1, result.leadershipExperiences().size());
        assertEquals(1, result.degrees().size());

        verify(memberMapper).toDto(calculation.getMember());
        verify(roleMapper).toDto(calculation.getRole());
        verify(certificateCalculationMapper).toDto(anyList());
        verify(leadershipExperienceCalculationMapper).toDto(anyList());
        verify(degreeCalculationMapper).toDto(anyList());
        verify(experienceCalculationMapper).toDto(anyList());
    }

    @DisplayName("Should map CalculationInputDto to Calculation")
    @ParameterizedTest
    @MethodSource("calculationInputDtoProvider")
    void shouldReturnCalculation(CalculationInputDto calculationInputDto) {
        Member member = createMember();
        Role role = createRole();

        when(memberBusinessService.getById(MEMBER_ID)).thenReturn(member);
        when(roleBusinessService.getById(ROLE_ID)).thenReturn(role);
        when(degreeCalculationMapper.fromDto(anyList())).thenReturn(List.of());
        when(experienceCalculationMapper.fromDto(anyList())).thenReturn(List.of());
        when(certificateCalculationMapper.fromDto(anyList())).thenReturn(List.of());

        Calculation result = calculationMapper.fromDto(calculationInputDto);

        assertNotNull(result);
        assertEquals(calculationInputDto.state(), result.getState());
        assertNull(result.getPublicizedBy());
        assertNull(result.getPublicationDate());
        assertEquals(member, result.getMember());
        assertEquals(role, result.getRole());
        assertEquals(0, result.getCertificates().size());
        assertEquals(0, result.getExperiences().size());
        assertEquals(0, result.getDegrees().size());

        verify(memberBusinessService).getById(MEMBER_ID);
        verify(roleBusinessService).getById(ROLE_ID);
        verify(degreeCalculationMapper).fromDto(anyList());
        verify(experienceCalculationMapper).fromDto(anyList());
        verify(certificateCalculationMapper).fromDto(anyList());
        verify(leadershipExperienceCalculationMapper).fromDto(anyList());

    }

    @DisplayName("Should map list of Calculations to CalculationDto list")
    @Test
    void shouldReturnListOfCalculationDto() {

        when(memberMapper.toDto(any(Member.class))).thenReturn(mock(MemberDto.class));
        when(roleMapper.toDto(any(Role.class))).thenReturn(mock(RoleDto.class));
        when(degreeCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(experienceCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(certificateCalculationMapper.toDto(anyList())).thenReturn(List.of());
        when(leadershipExperienceCalculationMapper.toDto(anyList())).thenReturn(List.of());

        List<CalculationDto> result = calculationMapper.toDto(List.of(calculation1, calculation2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(CALC_ID, result.get(0).id());
        assertEquals(CALC_2_ID, result.get(1).id());

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
        when(memberBusinessService.getById(anyLong())).thenReturn(mock(Member.class));
        when(roleBusinessService.getById(anyLong())).thenReturn(mock(Role.class));
        when(degreeCalculationMapper.fromDto(anyList())).thenReturn(List.of());
        when(experienceCalculationMapper.fromDto(anyList())).thenReturn(List.of());
        when(certificateCalculationMapper.fromDto(anyList())).thenReturn(List.of());

        List<Calculation> result = calculationMapper.fromDto(List.of(calculationInputDto1, calculationInputDto2));

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(memberBusinessService, times(2)).getById(MEMBER_ID);
        verify(roleBusinessService, times(2)).getById(ROLE_ID);
        verify(degreeCalculationMapper, times(2)).fromDto(anyList());
        verify(experienceCalculationMapper, times(2)).fromDto(anyList());
        verify(certificateCalculationMapper, times(2)).fromDto(anyList());
        verify(leadershipExperienceCalculationMapper, times(2)).fromDto(anyList());
    }
}
