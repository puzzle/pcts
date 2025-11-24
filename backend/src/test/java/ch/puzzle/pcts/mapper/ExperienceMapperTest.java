package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.Constants.EXPERIENCE_TYPE;
import static ch.puzzle.pcts.Constants.MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.experience.ExperienceInputDto;
import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.ExperienceTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class ExperienceMapperTest {

    private final LocalDate commonDate = LocalDate.of(2020, 1, 1);
    private final Long memberId = 1L;
    private final Long typeId = 10L;
    private ExperienceMapper mapper;
    private MemberMapper memberMapper;
    private ExperienceTypeMapper experienceTypeMapper;
    private MemberBusinessService memberBusinessService;
    private ExperienceTypeBusinessService experienceTypeBusinessService;
    private Member member;
    private MemberDto memberDto;
    private ExperienceType type;
    private ExperienceTypeDto typeDto;

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

        member = Member.Builder.builder().withId(memberId).build();
        memberDto = new MemberDto(memberId, "Susi", "Miller", null, null, null, null, null);

        type = new ExperienceType(typeId, "Management", new BigDecimal("10"), new BigDecimal("3"), new BigDecimal("8"));
        typeDto = new ExperienceTypeDto(typeId,
                                        "Management",
                                        new BigDecimal("10"),
                                        new BigDecimal("3"),
                                        new BigDecimal("8"));

        when(memberMapper.toDto(member)).thenReturn(memberDto);
        when(memberBusinessService.getById(memberId)).thenReturn(member);

        when(experienceTypeMapper.toDto(type)).thenReturn(typeDto);
        when(experienceTypeBusinessService.getById(typeId)).thenReturn(type);
    }

    @DisplayName("Should map Experience model to ExperienceDto correctly")
    @Test
    void shouldReturnExperienceDto() {
        Experience model = new Experience.Builder()
                .withId(5L)
                .withMember(member)
                .withName("Developer")
                .withEmployer("Puzzle ITC")
                .withPercent(80)
                .withType(type)
                .withComment("Good work")
                .withStartDate(commonDate)
                .withEndDate(commonDate.plusDays(10))
                .build();

        ExperienceDto result = mapper.toDto(model);

        assertEquals(model.getId(), result.id());
        assertEquals(memberDto, result.member());
        assertEquals(model.getName(), result.name());
        assertEquals(model.getEmployer(), result.employer());
        assertEquals(model.getPercent(), result.percent());
        assertEquals(typeDto, result.type());
        assertEquals(model.getComment(), result.comment());
        assertEquals(model.getStartDate(), result.startDate());
        assertEquals(model.getEndDate(), result.endDate());
    }

    @DisplayName("Should map ExperienceInputDto to Experience model correctly")
    @Test
    void shouldReturnExperienceModel() {
        ExperienceInputDto dto = new ExperienceInputDto(memberId,
                                                        "Consultant",
                                                        "Acme Corp",
                                                        100,
                                                        typeId,
                                                        "No notes",
                                                        commonDate,
                                                        commonDate.plusWeeks(2));

        Experience result = mapper.fromDto(dto);

        assertEquals(member, result.getMember());
        assertEquals(dto.name(), result.getName());
        assertEquals(dto.employer(), result.getEmployer());
        assertEquals(dto.percent(), result.getPercent());
        assertEquals(type, result.getType());
        assertEquals(dto.comment(), result.getComment());
        assertEquals(dto.startDate(), result.getStartDate());
        assertEquals(dto.endDate(), result.getEndDate());
    }

    @DisplayName("Should map list of Experience models to list of ExperienceDtos")
    @Test
    void shouldReturnListOfExperienceDtos() {
        Experience exp1 = new Experience.Builder().withMember(member).withType(type).build();
        Experience exp2 = new Experience.Builder().withMember(member).withType(type).build();
        List<Experience> models = List.of(exp1, exp2);

        List<ExperienceDto> result = mapper.toDto(models);

        assertEquals(2, result.size());
    }

    @DisplayName("Should map list of ExperienceInputDtos to list of Experience models")
    @Test
    void shouldReturnListOfExperienceModels() {
        ExperienceInputDto dto1 = new ExperienceInputDto(memberId,
                                                         "Role1",
                                                         "Employer1",
                                                         50,
                                                         typeId,
                                                         null,
                                                         commonDate,
                                                         null);
        ExperienceInputDto dto2 = new ExperienceInputDto(memberId,
                                                         "Role2",
                                                         "Employer2",
                                                         100,
                                                         typeId,
                                                         null,
                                                         commonDate,
                                                         null);
        List<ExperienceInputDto> dtos = List.of(dto1, dto2);

        List<Experience> result = mapper.fromDto(dtos);

        assertEquals(2, result.size());
        assertEquals("Role1", result.get(0).getName());
        assertEquals("Role2", result.get(1).getName());
    }

    @DisplayName("Should throw exception when member with id does not exist")
    @Test
    void shouldThrowExceptionWhenMemberWithIdDoesNotExist() {
        when(memberBusinessService.getById(5L)).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, Map.of(FieldKey.ENTITY, MEMBER, FieldKey.FIELD, "id",  FieldKey.IS, "5")))));

        ExperienceInputDto experienceInputDto = new ExperienceInputDto(5L,
                "Consultant",
                "Acme Corp",
                100,
                typeId,
                "No notes",
                commonDate,
                commonDate.plusWeeks(2));

        PCTSException exception = assertThrows(PCTSException.class, () -> mapper.fromDto(experienceInputDto));

        assertEquals(Map.of(FieldKey.ENTITY, MEMBER, FieldKey.FIELD, "id",  FieldKey.IS, "5"), exception.getErrors().get(0).getValues());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrors().get(0).getKey());
    }

    @DisplayName("Should throw exception when experienceType with id does not exist")
    @Test
    void shouldThrowExceptionWhenExperienceTypeWithIdDoesNotExist() {
        when(experienceTypeBusinessService.getById(5L)).thenThrow(new PCTSException(HttpStatus.NOT_FOUND, List.of(new GenericErrorDto(ErrorKey.NOT_FOUND, Map.of(FieldKey.ENTITY, EXPERIENCE_TYPE, FieldKey.FIELD, "id",  FieldKey.IS, "5")))));

        ExperienceInputDto experienceInputDto = new ExperienceInputDto(
                memberId,
                "Consultant",
                "Acme Corp",
                100,
                5L,
                "No notes",
                commonDate,
                commonDate.plusWeeks(2));

        PCTSException exception = assertThrows(PCTSException.class, () -> mapper.fromDto(experienceInputDto));

        assertEquals(Map.of(FieldKey.ENTITY, EXPERIENCE_TYPE, FieldKey.FIELD, "id",  FieldKey.IS, "5"), exception.getErrors().get(0).getValues());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrors().get(0).getKey());
    }
}
