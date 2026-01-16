package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.Constants.EXPERIENCE_TYPE;
import static ch.puzzle.pcts.Constants.MEMBER;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.experience.ExperienceInputDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.business.ExperienceTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class ExperienceMapperTest {

    private ExperienceMapper mapper;
    private MemberMapper memberMapper;
    private ExperienceTypeMapper experienceTypeMapper;
    private MemberBusinessService memberBusinessService;
    private ExperienceTypeBusinessService experienceTypeBusinessService;

    @BeforeEach
    void setUp() {
        memberMapper = Mockito.mock(MemberMapper.class);
        experienceTypeMapper = Mockito.mock(ExperienceTypeMapper.class);
        memberBusinessService = Mockito.mock(MemberBusinessService.class);
        experienceTypeBusinessService = Mockito.mock(ExperienceTypeBusinessService.class);

        mapper = new ExperienceMapper(memberMapper,
                                      experienceTypeMapper,
                                      memberBusinessService,
                                      experienceTypeBusinessService);

        when(memberMapper.toDto(MEMBER_1)).thenReturn(MEMBER_1_DTO);
        when(memberBusinessService.getById(MEMBER_1_ID)).thenReturn(MEMBER_1);
        when(experienceTypeMapper.toDto(EXP_TYPE_1)).thenReturn(EXP_TYPE_1_DTO);
        when(experienceTypeBusinessService.getById(EXP_TYPE_1_ID)).thenReturn(EXP_TYPE_1);
    }

    @DisplayName("Should map Experience model to ExperienceDto correctly")
    @Test
    void shouldReturnExperienceDto() {
        ExperienceDto result = mapper.toDto(EXPERIENCE_1);

        assertEquals(EXPERIENCE_1.getId(), result.id());
        assertEquals(MEMBER_1_DTO, result.member());
        assertEquals(EXPERIENCE_1.getName(), result.name());
        assertEquals(EXPERIENCE_1.getEmployer(), result.employer());
        assertEquals(EXPERIENCE_1.getPercent(), result.percent());
        assertEquals(EXP_TYPE_1_DTO, result.type());
        assertEquals(EXPERIENCE_1.getComment(), result.comment());
        assertEquals(EXPERIENCE_1.getStartDate(), result.startDate());
        assertEquals(EXPERIENCE_1.getEndDate(), result.endDate());
    }

    @DisplayName("Should map ExperienceInputDto to Experience model correctly")
    @Test
    void shouldReturnExperienceModel() {
        Experience result = mapper.fromDto(EXPERIENCE_1_INPUT);

        assertEquals(EXPERIENCE_1_INPUT.memberId(), result.getMember().getId());
        assertEquals(EXPERIENCE_1_INPUT.name(), result.getName());
        assertEquals(EXPERIENCE_1_INPUT.employer(), result.getEmployer());
        assertEquals(EXPERIENCE_1_INPUT.percent(), result.getPercent());
        assertEquals(EXPERIENCE_1_INPUT.experienceTypeId(), result.getType().getId());
        assertEquals(EXPERIENCE_1_INPUT.comment(), result.getComment());
        assertEquals(EXPERIENCE_1_INPUT.startDate(), result.getStartDate());
        assertEquals(EXPERIENCE_1_INPUT.endDate(), result.getEndDate());
    }

    @DisplayName("Should map list of Experience models to list of ExperienceDtos")
    @Test
    void shouldReturnListOfExperienceDtos() {
        List<ExperienceDto> result = mapper.toDto(List.of(EXPERIENCE_1, EXPERIENCE_2));

        assertEquals(2, result.size());
    }

    @DisplayName("Should map list of ExperienceInputDtos to list of Experience models")
    @Test
    void shouldReturnListOfExperienceModels() {
        List<Experience> result = mapper.fromDto(List.of(EXPERIENCE_1_INPUT, EXPERIENCE_2_INPUT));

        assertEquals(2, result.size());
        assertEquals(EXPERIENCE_1_INPUT.name(), result.get(0).getName());
        assertEquals(EXPERIENCE_2_INPUT.name(), result.get(1).getName());
    }

    @DisplayName("Should throw exception when member with id does not exist")
    @Test
    void shouldThrowExceptionWhenMemberWithIdDoesNotExist() {
        when(memberBusinessService.getById(INVALID_ID)).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, Map.of(FieldKey.ENTITY, MEMBER, FieldKey.FIELD, "id",  FieldKey.IS, String.valueOf(INVALID_ID))))));

        ExperienceInputDto experienceInputDto = new ExperienceInputDto(
                INVALID_ID,
                "Consultant",
                "Acme Corp",
                100,
                EXP_TYPE_1_ID,
                "No notes",
                DATE_NOW,
                DATE_NOW);

        PCTSException exception = assertThrows(PCTSException.class, () -> mapper.fromDto(experienceInputDto));

        assertEquals(Map.of(FieldKey.ENTITY, MEMBER, FieldKey.FIELD, "id",  FieldKey.IS, String.valueOf(INVALID_ID)), exception.getErrors().get(0).values());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrors().get(0).key());
    }

    @DisplayName("Should throw exception when experienceType with id does not exist")
    @Test
    void shouldThrowExceptionWhenExperienceTypeWithIdDoesNotExist() {
        when(experienceTypeBusinessService.getById(INVALID_ID)).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, Map.of(FieldKey.ENTITY, EXPERIENCE_TYPE, FieldKey.FIELD, "id",  FieldKey.IS, String.valueOf(INVALID_ID))))));

        ExperienceInputDto experienceInputDto = new ExperienceInputDto(
                MEMBER_1_ID,
                "Consultant",
                "Acme Corp",
                100,
                INVALID_ID,
                "No notes",
                DATE_NOW,
                DATE_NOW);

        PCTSException exception = assertThrows(PCTSException.class, () -> mapper.fromDto(experienceInputDto));

        assertEquals(Map.of(FieldKey.ENTITY, EXPERIENCE_TYPE, FieldKey.FIELD, "id",  FieldKey.IS, String.valueOf(INVALID_ID)), exception.getErrors().get(0).values());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrors().get(0).key());
    }
}
