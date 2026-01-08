package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.MemberMapper;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@ControllerIT(MemberController.class)
class MemberControllerIT extends ControllerITBase {

    @MockitoBean
    private MemberBusinessService service;

    @MockitoBean
    private MemberMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/members";

    private final LocalDate commonDate = LocalDate.of(2019, 8, 4);

    private Member member;
    private MemberInputDto requestDto;
    private MemberDto expectedDto;
    private Long id;

    @BeforeEach
    void setUp() {
        id = 1L;
        OrganisationUnitDto organisationUnitDto = new OrganisationUnitDto(1L, "/bbt");
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "/bbt");

        member = Member.Builder
                .builder()
                .withId(id)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();

        requestDto = new MemberInputDto("Susi",
                                        "Miller",
                                        EmploymentState.APPLICANT,
                                        "SM",
                                        LocalDate.MIN,
                                        commonDate,
                                        null,
                                        1L);

        expectedDto = new MemberDto(id,
                                    "Susi",
                                    "Miller",
                                    EmploymentState.APPLICANT,
                                    "SM",
                                    commonDate,
                                    commonDate,
                                    "miller@puzzle.ch",
                                    organisationUnitDto);
    }

    @DisplayName("Should successfully get all members")
    @Test
    void shouldGetAllMembers() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(member));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        mvc
                .perform(get(BASEURL).with(csrf()).with(adminJwt()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get member by id as an admin")
    @Test
    void shouldGetMemberByIdAsAnAdmin() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(member);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/" + id).with(csrf()).with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getById(id);
        verify(mapper, times(1)).toDto(any(Member.class));
    }

    @DisplayName("Should successfully get member by id as the owner")
    @Test
    void shouldGetMemberByIdAsOwner() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(member);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/" + id).with(csrf()).with(ownerJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getById(id);
        verify(mapper, times(1)).toDto(any(Member.class));
    }

    @DisplayName("Should successfully create new member")
    @Test
    void shouldCreateNewMember() throws Exception {
        BDDMockito.given(mapper.fromDto(any(MemberInputDto.class))).willReturn(member);
        BDDMockito.given(service.create(any(Member.class))).willReturn(member);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(MemberInputDto.class));
        verify(service, times(1)).create(any(Member.class));
        verify(mapper, times(1)).toDto(any(Member.class));
    }

    @DisplayName("Should successfully update member")
    @Test
    void shouldUpdateMember() throws Exception {
        BDDMockito.given(mapper.fromDto(any(MemberInputDto.class))).willReturn(member);
        BDDMockito.given(service.update(any(Long.class), any(Member.class))).willReturn(member);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/" + id)
                        .content(jsonMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(MemberInputDto.class));
        verify(service, times(1)).update(any(Long.class), any(Member.class));
        verify(mapper, times(1)).toDto(any(Member.class));
    }

    @DisplayName("Should successfully delete member")
    @Test
    void shouldDeleteMember() throws Exception {
        BDDMockito.willDoNothing().given(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }

    @DisplayName("Should successfully get myself as a member")
    @Test
    void shouldSuccessfullyGetMyselfAsAMember() throws Exception {
        BDDMockito.given(service.getLoggedInMember()).willReturn(member);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/myself").with(csrf()).with(ownerJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getLoggedInMember();
        verify(mapper, times(1)).toDto(any(Member.class));
    }
}
