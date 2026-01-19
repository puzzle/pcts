package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MemberMapperTest {

    private MemberMapper mapper;
    private OrganisationUnitBusinessService organisationUnitBusinessService;
    private OrganisationUnitMapper organisationUnitMapper;

    @BeforeEach
    void setUp() {
        organisationUnitBusinessService = Mockito.mock(OrganisationUnitBusinessService.class);
        organisationUnitMapper = Mockito.mock(OrganisationUnitMapper.class);
        mapper = new MemberMapper(organisationUnitBusinessService, organisationUnitMapper);

        when(organisationUnitMapper.toDto(ORG_UNIT_1)).thenReturn(ORG_UNIT_1_DTO);
        when(organisationUnitMapper.toDto(ORG_UNIT_2)).thenReturn(ORG_UNIT_2_DTO);
        when(organisationUnitBusinessService.getReferenceById(ORG_UNIT_1_ID)).thenReturn(ORG_UNIT_1);
        when(organisationUnitBusinessService.getReferenceById(ORG_UNIT_2_ID)).thenReturn(ORG_UNIT_2);
    }

    @DisplayName("Should map Member model to MemberDto correctly")
    @Test
    void shouldReturnMemberDto() {
        MemberDto result = mapper.toDto(MEMBER_1);

        assertEquals(MEMBER_1_DTO, result);
    }

    @DisplayName("Should map MemberInputDto to Member model correctly")
    @Test
    void shouldReturnMemberModel() {
        Member result = mapper.fromDto(MEMBER_1_INPUT);

        assertEquals(MEMBER_1_INPUT.firstName(), result.getFirstName());
        assertEquals(MEMBER_1_INPUT.lastName(), result.getLastName());
        assertEquals(MEMBER_1_INPUT.employmentState(), result.getEmploymentState());
        assertEquals(MEMBER_1_INPUT.abbreviation(), result.getAbbreviation());
        assertEquals(MEMBER_1_INPUT.dateOfHire(), result.getDateOfHire());
        assertEquals(MEMBER_1_INPUT.birthDate(), result.getBirthDate());
        assertEquals(ORG_UNIT_1, result.getOrganisationUnit());
    }

    @DisplayName("Should map list of Member models to list of MemberDtos")
    @Test
    void shouldReturnListOfMemberDtos() {
        List<MemberDto> result = mapper.toDto(List.of(MEMBER_1, MEMBER_2));

        assertEquals(2, result.size());
        assertEquals(MEMBER_1_DTO, result.getFirst());
        assertEquals(MEMBER_2_DTO, result.getLast());
    }

    @DisplayName("Should map list of MemberInputDtos to list of Member models")
    @Test
    void shouldReturnListOfMembers() {

        List<Member> result = mapper.fromDto(List.of(MEMBER_1_INPUT, MEMBER_2_INPUT));

        assertEquals(2, result.size());
        Member resultModel1 = result.get(0);
        assertEquals(MEMBER_1_INPUT.firstName(), resultModel1.getFirstName());
        assertEquals(MEMBER_1_INPUT.employmentState(), resultModel1.getEmploymentState());
        assertEquals(ORG_UNIT_1, resultModel1.getOrganisationUnit());

        Member resultModel2 = result.get(1);
        assertEquals(MEMBER_2_INPUT.abbreviation(), resultModel2.getAbbreviation());
    }

    @DisplayName("Should return null if OrganisationUnitId is null")
    @Test
    void shouldReturnNullIfOrganisationUnitIsNull() {
        OrganisationUnit result = mapper.organisationUnitFromId(null);
        assertNull(result);
    }

    @DisplayName("Should return OrganisationUnit if OrganisationUnit exists")
    @Test
    void shouldReturnOrganisationUnitIfOrganisationUnitExists() {
        OrganisationUnit result = mapper.organisationUnitFromId(ORG_UNIT_1.getId());
        assertEquals(ORG_UNIT_1, result);
    }
}