package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.Constants.*;
import static ch.puzzle.pcts.util.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.service.business.DegreeTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class DegreeMapperTest {
    private DegreeMapper mapper;

    @BeforeEach
    void setUp() {
        MemberBusinessService memberBusinessService = Mockito.mock(MemberBusinessService.class);
        DegreeTypeBusinessService degreeTypeBusinessService = Mockito.mock(DegreeTypeBusinessService.class);
        DegreeTypeMapper degreeTypeMapper = Mockito.mock(DegreeTypeMapper.class);
        MemberMapper memberMapper = Mockito.mock(MemberMapper.class);
        mapper = new DegreeMapper(memberBusinessService, degreeTypeBusinessService, degreeTypeMapper, memberMapper);

        when(degreeTypeMapper.toDto(DEGREE_TYPE_1)).thenReturn(DEGREE_TYPE_1_DTO);
        when(degreeTypeBusinessService.getById(DEGREE_TYPE_1_ID)).thenReturn(DEGREE_TYPE_1);
        when(memberMapper.toDto(MEMBER_1)).thenReturn(MEMBER_1_DTO);
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
    }

    @DisplayName("Should return degree")
    @Test
    void shouldReturnDegree() {
        Degree result = mapper.fromDto(DEGREE_1_INPUT);

        assertEquals(MEMBER_1, result.getMember());
        assertEquals(DEGREE_1_INPUT.name(), result.getName());
        assertEquals(DEGREE_1_INPUT.institution(), result.getInstitution());
        assertEquals(DEGREE_1_INPUT.completed(), result.getCompleted());
        assertEquals(DEGREE_TYPE_1, result.getDegreeType());
        assertEquals(DEGREE_1_INPUT.startDate(), result.getStartDate());
        assertEquals(DEGREE_1_INPUT.endDate(), result.getEndDate());
        assertEquals(DEGREE_1_INPUT.comment(), result.getComment());
    }

    @DisplayName("Should return degree dto")
    @Test
    void shouldReturnDegreeDto() {
        DegreeDto result = mapper.toDto(DEGREE_1);

        assertEquals(DEGREE_1.getId(), result.id());
        assertEquals(MEMBER_1_DTO, result.member());
        assertEquals(DEGREE_1.getName(), result.name());
        assertEquals(DEGREE_1.getInstitution(), result.institution());
        assertEquals(DEGREE_1.getCompleted(), result.completed());
        assertEquals(DEGREE_TYPE_1_DTO, result.type());
        assertEquals(DEGREE_1.getStartDate(), result.startDate());
        assertEquals(DEGREE_1.getEndDate(), result.endDate());
        assertEquals(DEGREE_1.getComment(), result.comment());
    }

    @DisplayName("Should return list of degrees")
    @Test
    void shouldReturnListOfDegree() {
        List<Degree> result = mapper.fromDto(List.of(DEGREE_1_INPUT, DEGREE_2_INPUT));

        assertEquals(2, result.size());

        Degree first = result.getFirst();
        assertEquals(DEGREE_1.getName(), first.getName());
        assertEquals(DEGREE_1.getInstitution(), first.getInstitution());
        assertEquals(DEGREE_1.getCompleted(), first.getCompleted());
        assertEquals(DEGREE_1.getComment(), first.getComment());

        Degree second = result.get(1);
        assertEquals(DEGREE_2.getName(), second.getName());
        assertEquals(DEGREE_2.getInstitution(), second.getInstitution());
        assertEquals(DEGREE_2.getCompleted(), second.getCompleted());
        assertEquals(DEGREE_2.getComment(), second.getComment());
    }

    @DisplayName("Should return list of degree dto")
    @Test
    void shouldReturnListOfDegreeDto() {
        List<DegreeDto> result = mapper.toDto(List.of(DEGREE_1, DEGREE_2));

        assertEquals(2, result.size());

        DegreeDto first = result.getFirst();
        assertEquals(DEGREE_1.getName(), first.name());
        assertEquals(DEGREE_1.getInstitution(), first.institution());
        assertEquals(DEGREE_1.getCompleted(), first.completed());
        assertEquals(DEGREE_1.getComment(), first.comment());

        DegreeDto second = result.get(1);
        assertEquals(DEGREE_2.getName(), second.name());
        assertEquals(DEGREE_2.getInstitution(), second.institution());
        assertEquals(DEGREE_2.getCompleted(), second.completed());
        assertEquals(DEGREE_2.getComment(), second.comment());
    }

    @DisplayName("Should throw exception when member id is invalid")
    @Test
    void shouldThrowExceptionWhenMemberIdIsInvalid() {
        DegreeInputDto invalidInput = new DegreeInputDto(INVALID_ID,
                                                         "Degree Invalid",
                                                         "Institution X",
                                                         true,
                                                         DEGREE_TYPE_1_ID,
                                                         DATE_NOW,
                                                         DATE_NOW,
                                                         "Invalid Member");

        Map<FieldKey, String> attributes = Map
                .of(FieldKey.ENTITY,
                    MEMBER,
                    FieldKey.FIELD,
                    "id",
                    FieldKey.IS,
                    String.valueOf(invalidInput.memberId()));

        // mock the behavior
        when(mapper.memberBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, attributes))));

        assertThrows(PCTSException.class, () -> mapper.fromDto(invalidInput));
    }

    @DisplayName("Should throw exception when degree type id is invalid")
    @Test
    void shouldThrowExceptionWhenDegreeTypeIdIsInvalid() {
        DegreeInputDto invalidInput = new DegreeInputDto(MEMBER_1_ID,
                                                         "Degree Invalid",
                                                         "Institution X",
                                                         true,
                                                         INVALID_ID,
                                                         DATE_NOW,
                                                         DATE_NOW,
                                                         "Invalid Degree Type");

        Map<FieldKey, String> attributes = Map
                .of(FieldKey.ENTITY,
                    DEGREE_TYPE,
                    FieldKey.FIELD,
                    "id",
                    FieldKey.IS,
                    String.valueOf(invalidInput.typeId()));

        // mock the behavior
        when(mapper.degreeTypeBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, attributes))));

        assertThrows(PCTSException.class, () -> mapper.fromDto(invalidInput));
    }

}
