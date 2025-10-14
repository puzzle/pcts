package ch.puzzle.pcts.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MemberMapperTest {

    private MemberMapper mapper;
    private OrganisationUnitBusinessService organisationUnitBusinessService;
    private OrganisationUnitMapper organisationUnitMapper;

    private OrganisationUnit organisationUnit;
    private OrganisationUnitDto organisationUnitDto;
    private Member member1;
    private final Date commonDate = new Date(0L);
    private final Long orgUnitId = 1L;

    @BeforeEach
    void setUp() {
        organisationUnitBusinessService = Mockito.mock(OrganisationUnitBusinessService.class);
        organisationUnitMapper = Mockito.mock(OrganisationUnitMapper.class);
        mapper = new MemberMapper(organisationUnitBusinessService, organisationUnitMapper);

        organisationUnit = new OrganisationUnit(orgUnitId, "/bbt");
        organisationUnitDto = new OrganisationUnitDto(orgUnitId, "/bbt");

        member1 = Member.Builder
                .builder()
                .withId(1L)
                .withName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();

        when(organisationUnitMapper.toDto(organisationUnit)).thenReturn(organisationUnitDto);
        when(organisationUnitBusinessService.getById(orgUnitId)).thenReturn(organisationUnit);
    }

    @DisplayName("Should map Member model to MemberDto correctly")
    @Test
    void shouldReturnMemberDto() {
        Member modelWithApplicantState = Member.Builder
                .builder()
                .withId(1L)
                .withName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();

        MemberDto result = mapper.toDto(modelWithApplicantState);

        assertEquals(modelWithApplicantState.getId(), result.id());
        assertEquals(modelWithApplicantState.getName(), result.name());
        assertEquals(modelWithApplicantState.getLastName(), result.lastName());
        assertEquals(modelWithApplicantState.getEmploymentState(), result.employmentState());
        assertEquals(modelWithApplicantState.getAbbreviation(), result.abbreviation());
        assertEquals(modelWithApplicantState.getDateOfHire(), result.dateOfHire());
        assertEquals(modelWithApplicantState.getBirthDate(), result.birthDate());
        assertEquals(organisationUnitDto, result.organisationUnit());
    }

    @DisplayName("Should map MemberInputDto to Member model correctly")
    @Test
    void shouldReturnMemberModel() {
        MemberInputDto dtoWithExMemberState = new MemberInputDto("Jane",
                                                                 "Smith",
                                                                 EmploymentState.EX_MEMBER,
                                                                 "JS",
                                                                 commonDate,
                                                                 commonDate,
                                                                 orgUnitId);

        Member result = mapper.fromDto(dtoWithExMemberState);

        assertEquals(dtoWithExMemberState.name(), result.getName());
        assertEquals(dtoWithExMemberState.lastName(), result.getLastName());
        assertEquals(dtoWithExMemberState.employmentState(), result.getEmploymentState());
        assertEquals(dtoWithExMemberState.abbreviation(), result.getAbbreviation());
        assertEquals(dtoWithExMemberState.dateOfHire(), result.getDateOfHire());
        assertEquals(dtoWithExMemberState.birthDate(), result.getBirthDate());
        assertEquals(organisationUnit, result.getOrganisationUnit());
    }

    @DisplayName("Should map list of Member models to list of MemberDtos")
    @Test
    void shouldReturnListOfMemberDtos() {
        Member member2 = Member.Builder
                .builder()
                .withId(1L)
                .withName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();
        List<Member> members = List.of(member1, member2);

        List<MemberDto> result = mapper.toDto(members);

        assertEquals(2, result.size());
        MemberDto resultDto1 = result.get(0);
        assertEquals(member1.getId(), resultDto1.id());
        assertEquals(member1.getName(), resultDto1.name());
        assertEquals(member1.getEmploymentState(), resultDto1.employmentState());
        assertEquals(organisationUnitDto, resultDto1.organisationUnit());

        MemberDto resultDto2 = result.get(1);
        assertEquals(member2.getId(), resultDto2.id());
    }

    @DisplayName("Should map list of MemberInputDtos to list of Member models")
    @Test
    void shouldReturnListOfMembers() {
        MemberInputDto dto1 = new MemberInputDto(member1.getName(),
                                                 member1.getLastName(),
                                                 member1.getEmploymentState(),
                                                 member1.getAbbreviation(),
                                                 member1.getDateOfHire(),
                                                 member1.getBirthDate(),
                                                 orgUnitId);
        MemberInputDto dto2 = new MemberInputDto("Jane",
                                                 "Smith",
                                                 EmploymentState.MEMBER,
                                                 "JS",
                                                 commonDate,
                                                 commonDate,
                                                 orgUnitId);
        List<MemberInputDto> dtos = List.of(dto1, dto2);

        List<Member> result = mapper.fromDto(dtos);

        assertEquals(2, result.size());
        Member resultModel1 = result.get(0);
        assertEquals(dto1.name(), resultModel1.getName());
        assertEquals(dto1.employmentState(), resultModel1.getEmploymentState());
        assertEquals(organisationUnit, resultModel1.getOrganisationUnit());

        Member resultModel2 = result.get(1);
        assertEquals(dto2.abbreviation(), resultModel2.getAbbreviation());
    }
}