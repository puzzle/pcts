package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.DegreeTypeBusinessService;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

public class DegreeMapperTest {
    private DegreeMapper mapper;

    private Member member;
    private MemberDto memberDto;
    private DegreeType degreeType;
    private DegreeTypeDto degreeTypeDto;

    private final Long degreeId = 1L;
    private final Long degreeTypeId = 1L;
    private final Long memberId = 1L;

    private final LocalDate commonDate = LocalDate.EPOCH;

    @BeforeEach
    void setUp() {
        MemberBusinessService memberBusinessService = Mockito.mock(MemberBusinessService.class);
        DegreeTypeBusinessService degreeTypeBusinessService = Mockito.mock(DegreeTypeBusinessService.class);
        DegreeTypeMapper degreeTypeMapper = Mockito.mock(DegreeTypeMapper.class);
        MemberMapper memberMapper = Mockito.mock(MemberMapper.class);
        mapper = new DegreeMapper(memberBusinessService, degreeTypeBusinessService, degreeTypeMapper, memberMapper);

        degreeType = new DegreeType(degreeTypeId,
                                    "Degree Type 1",
                                    new BigDecimal("3.0"),
                                    new BigDecimal("2.0"),
                                    new BigDecimal("1.0"));
        degreeTypeDto = new DegreeTypeDto(degreeTypeId,
                                          "Degree Type 1",
                                          new BigDecimal("3.0"),
                                          new BigDecimal("2.0"),
                                          new BigDecimal("1.0"));

        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "/bbt");
        OrganisationUnitDto organisationUnitDto = new OrganisationUnitDto(1L, "/bbt");

        member = Member.Builder
                .builder()
                .withId(memberId)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();
        memberDto = new MemberDto(memberId,
                                  "Susi",
                                  "Miller",
                                  EmploymentState.APPLICANT,
                                  "SM",
                                  commonDate,
                                  commonDate,
                                  organisationUnitDto);

        when(degreeTypeMapper.toDto(degreeType)).thenReturn(degreeTypeDto);
        when(degreeTypeBusinessService.getById(degreeTypeId)).thenReturn(degreeType);
        when(memberMapper.toDto(member)).thenReturn(memberDto);
        when(memberBusinessService.getById(memberId)).thenReturn(member);
    }

    @DisplayName("Should return degree")
    @Test
    void shouldReturnDegree() {
        DegreeInputDto degreeInputDto = new DegreeInputDto(memberId,
                                                           "Degree 1",
                                                           "Institution 1",
                                                           true,
                                                           degreeTypeId,
                                                           commonDate,
                                                           commonDate,
                                                           "Comment 1");

        Degree result = mapper.fromDto(degreeInputDto);

        assertEquals(member, result.getMember());
        assertEquals(degreeInputDto.name(), result.getName());
        assertEquals(degreeInputDto.institution(), result.getInstitution());
        assertEquals(degreeInputDto.completed(), result.getCompleted());
        assertEquals(degreeType, result.getType());
        assertEquals(degreeInputDto.startDate(), result.getStartDate());
        assertEquals(degreeInputDto.endDate(), result.getEndDate());
        assertEquals(degreeInputDto.comment(), result.getComment());
    }

    @DisplayName("Should return degree dto")
    @Test
    void shouldReturnDegreeDto() {
        Degree degree = Degree.Builder
                .builder()
                .withId(degreeId)
                .withMember(member)
                .withName("Degree 1")
                .withInstitution("Institution 1")
                .withCompleted(true)
                .withType(degreeType)
                .withStartDate(commonDate)
                .withEndDate(commonDate)
                .withComment("Comment 1")
                .build();

        DegreeDto result = mapper.toDto(degree);

        assertEquals(degree.getId(), result.id());
        assertEquals(memberDto, result.member());
        assertEquals(degree.getName(), result.name());
        assertEquals(degree.getInstitution(), result.institution());
        assertEquals(degree.getCompleted(), result.completed());
        assertEquals(degreeTypeDto, result.type());
        assertEquals(degree.getStartDate(), result.startDate());
        assertEquals(degree.getEndDate(), result.endDate());
        assertEquals(degree.getComment(), result.comment());
    }

    @DisplayName("Should return list of degrees")
    @Test
    void shouldReturnListOfDegree() {
        DegreeInputDto degreeInputDto1 = new DegreeInputDto(memberId,
                                                            "Degree 1",
                                                            "Institution 1",
                                                            true,
                                                            degreeTypeId,
                                                            commonDate,
                                                            commonDate,
                                                            "Comment 1");
        DegreeInputDto degreeInputDto2 = new DegreeInputDto(memberId,
                                                            "Degree 2",
                                                            "Institution 2",
                                                            false,
                                                            degreeTypeId,
                                                            commonDate,
                                                            commonDate,
                                                            "Comment 2");

        List<DegreeInputDto> inputList = List.of(degreeInputDto1, degreeInputDto2);

        List<Degree> result = mapper.fromDto(inputList);

        assertEquals(2, result.size());

        Degree first = result.getFirst();
        assertEquals("Degree 1", first.getName());
        assertEquals("Institution 1", first.getInstitution());
        assertEquals(true, first.getCompleted());
        assertEquals("Comment 1", first.getComment());

        Degree second = result.get(1);
        assertEquals("Degree 2", second.getName());
        assertEquals("Institution 2", second.getInstitution());
        assertEquals(false, second.getCompleted());
        assertEquals("Comment 2", second.getComment());
    }

    @DisplayName("Should return list of degree dto")
    @Test
    void shouldReturnListOfDegreeDto() {
        Degree degree1 = Degree.Builder
                .builder()
                .withId(degreeId)
                .withMember(member)
                .withName("Degree 1")
                .withInstitution("Institution 1")
                .withCompleted(true)
                .withType(degreeType)
                .withStartDate(commonDate)
                .withEndDate(commonDate)
                .withComment("Comment 1")
                .build();

        Degree degree2 = Degree.Builder
                .builder()
                .withId(2L)
                .withMember(member)
                .withName("Degree 2")
                .withInstitution("Institution 2")
                .withCompleted(false)
                .withType(degreeType)
                .withStartDate(commonDate)
                .withEndDate(commonDate)
                .withComment("Comment 2")
                .build();

        List<Degree> degreeList = List.of(degree1, degree2);

        List<DegreeDto> result = mapper.toDto(degreeList);

        assertEquals(2, result.size());

        DegreeDto first = result.getFirst();
        assertEquals("Degree 1", first.name());
        assertEquals("Institution 1", first.institution());
        assertEquals(true, first.completed());
        assertEquals("Comment 1", first.comment());

        DegreeDto second = result.get(1);
        assertEquals("Degree 2", second.name());
        assertEquals("Institution 2", second.institution());
        assertEquals(false, second.completed());
        assertEquals("Comment 2", second.comment());
    }

    @DisplayName("Should throw exception when member id is invalid")
    @Test
    void shouldThrowExceptionWhenMemberIdIsInvalid() {
        DegreeInputDto invalidInput = new DegreeInputDto(999L,
                                                         "Degree Invalid",
                                                         "Institution X",
                                                         true,
                                                         degreeTypeId,
                                                         commonDate,
                                                         commonDate,
                                                         "Invalid Member");

        // mock the behavior
        when(mapper.memberBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             "Member with id: " + invalidInput.memberId() + " does not exist.",
                                             ErrorKey.NOT_FOUND));

        assertThrows(PCTSException.class, () -> mapper.fromDto(invalidInput));
    }

    @DisplayName("Should throw exception when degree type id is invalid")
    @Test
    void shouldThrowExceptionWhenDegreeTypeIdIsInvalid() {
        DegreeInputDto invalidInput = new DegreeInputDto(memberId,
                                                         "Degree Invalid",
                                                         "Institution X",
                                                         true,
                                                         999L,
                                                         commonDate,
                                                         commonDate,
                                                         "Invalid Degree Type");

        // mock the behavior
        when(mapper.degreeTypeBusinessService.getById(anyLong()))
                .thenThrow(new PCTSException(HttpStatus.NOT_FOUND,
                                             "DegreeType with id: " + invalidInput.typeId() + " does not exist.",
                                             ErrorKey.NOT_FOUND));

        assertThrows(PCTSException.class, () -> mapper.fromDto(invalidInput));
    }

}
