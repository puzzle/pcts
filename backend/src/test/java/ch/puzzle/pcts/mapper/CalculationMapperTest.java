package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.service.business.RoleBusinessService;
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

    @DisplayName("Should map Calculation to CalculationDto")
    @Test
    void shouldReturnCalculationDto() {
        when(memberMapper.toDto(MEMBER_1)).thenReturn(MEMBER_1_DTO);
        when(roleMapper.toDto(ROLE_2)).thenReturn(ROLE_2_DTO);

        CalculationDto result = calculationMapper.toDto(CALCULATION_1);

        assertNotNull(result);
        assertEquals(CALCULATION_1_ID, result.id());
        assertEquals(CALCULATION_1.getState(), result.state());
        assertEquals(CALCULATION_1.getPublicationDate(), result.publicationDate());
        assertEquals(CALCULATION_1.getPublicizedBy(), result.publicizedBy());
        assertEquals(MEMBER_1_DTO, result.member());
        assertEquals(ROLE_2_DTO, result.role());

        verify(memberMapper).toDto(MEMBER_1);
        verify(roleMapper).toDto(ROLE_2);
    }

    @DisplayName("Should map CalculationInputDto to Calculation")
    @Test
    void shouldReturnCalculation() {
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(roleBusinessService.getById(ROLE_2_ID)).thenReturn(ROLE_2);

        Calculation result = calculationMapper.fromDto(CALCULATION_INPUT_DTO_1);

        assertNotNull(result);
        assertEquals(CALCULATION_INPUT_DTO_1.state(), result.getState());
        assertNull(result.getPublicizedBy());
        assertNull(result.getPublicationDate());
        assertEquals(MEMBER_1, result.getMember());
        assertEquals(ROLE_2, result.getRole());

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(roleBusinessService).getById(ROLE_2_ID);
    }

    @DisplayName("Should map list of Calculations to CalculationDto list")
    @Test
    void shouldReturnListOfCalculationDto() {
        when(memberMapper.toDto(any(Member.class))).thenReturn(mock(MemberDto.class));
        when(roleMapper.toDto(any(Role.class))).thenReturn(mock(RoleDto.class));

        List<CalculationDto> result = calculationMapper.toDto(List.of(CALCULATION_1, CALCULATION_2));

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
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(memberBusinessService.getById(MEMBER_2_ID)).thenReturn(MEMBER_2);
        when(roleBusinessService.getById(ROLE_2_ID)).thenReturn(ROLE_2);

        List<Calculation> result = calculationMapper.fromDto(List.of(CALCULATION_INPUT_DTO_1, CALCULATION_INPUT_DTO_2));

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(memberBusinessService).getById(MEMBER_1_ID);
        verify(memberBusinessService).getById(MEMBER_2_ID);
        verify(roleBusinessService, times(2)).getById(ROLE_2_ID);
    }

    @DisplayName("Should throw when Member not found")
    @Test
    void shouldThrowWhenMemberNotFound() {
        when(memberBusinessService.getById(anyLong())).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of()));

        assertThrows(PCTSException.class, () -> calculationMapper.fromDto(CALCULATION_INPUT_DTO_1));
    }

    @DisplayName("Should throw when Role not found")
    @Test
    void shouldThrowWhenRoleNotFound() {
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(roleBusinessService.getById(anyLong())).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of()));

        assertThrows(PCTSException.class, () -> calculationMapper.fromDto(CALCULATION_INPUT_DTO_1));
    }
}
