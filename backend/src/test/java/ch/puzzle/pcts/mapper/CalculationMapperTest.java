package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class CalculationMapperTest {

    private static final Long MEMBER_ID = 1L;
    private static final Long ROLE_ID = 2L;
    private static final Long CALC_ID = 3L;

    private static final CalculationState STATE = CalculationState.ACTIVE;
    private static final LocalDate PUBLICATION_DATE = LocalDate.now();
    private static final String PUBLICIZED_BY = "Ldap User";

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private MemberBusinessService memberBusinessService;

    @Mock
    private RoleBusinessService roleBusinessService;

    private CalculationMapper calculationMapper;

    @BeforeEach
    void setUp() {
        calculationMapper = new CalculationMapper(memberMapper, roleMapper, memberBusinessService, roleBusinessService);
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
        assertEquals(member, result.getMember());
        assertEquals(role, result.getRole());

        verify(memberBusinessService).getById(MEMBER_ID);
        verify(roleBusinessService).getById(ROLE_ID);
    }

    @DisplayName("Should map list of Calculations to CalculationDto list")
    @Test
    void shouldReturnListOfCalculationDto() {
        Calculation c1 = new Calculation(1L, new Member(), new Role(), STATE, null, null);
        Calculation c2 = new Calculation(2L, new Member(), new Role(), STATE, null, null);

        when(memberMapper.toDto(any(Member.class))).thenReturn(mock(MemberDto.class));
        when(roleMapper.toDto(any(Role.class))).thenReturn(mock(RoleDto.class));

        List<CalculationDto> result = calculationMapper.toDto(List.of(c1, c2));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        verify(memberMapper, times(2)).toDto(any(Member.class));
        verify(roleMapper, times(2)).toDto(any(Role.class));
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

    @DisplayName("Should throw when Member not found")
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
