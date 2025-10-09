package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.MemberMapper;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MemberController.class)
class MemberControllerIT {

    @MockitoBean
    private MemberBusinessService service;

    @MockitoBean
    private MemberMapper mapper;

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASEURL = "/api/v1/members";

    private Member member;
    private MemberInputDto requestDto;
    private MemberDto expectedDto;
    private Long id;

    private OrganisationUnit organisationUnit;
    private OrganisationUnitDto organisationUnitDto;
    private Date dateOfHire;
    private Date birthDate;

    @BeforeEach
    void setUp() {
        id = 1L;
        organisationUnitDto = new OrganisationUnitDto(1L, "/bbt");
        organisationUnit = new OrganisationUnit(1L, "/bbt");
        dateOfHire = new Date();
        birthDate = new Date();

        member = new Member(id,
                            "Susi",
                            "Sommer",
                            EmploymentState.APPLICANT,
                            "SS",
                            dateOfHire,
                            birthDate,
                            true,
                            organisationUnit

        );

        requestDto = new MemberInputDto(null,
                                        "Susi",
                                        "Sommer",
                                        EmploymentState.APPLICANT,
                                        "SS",
                                        dateOfHire,
                                        birthDate,
                                        true,
                                        1L);

        expectedDto = new MemberDto(id,
                                    "Susi",
                                    "Sommer",
                                    EmploymentState.APPLICANT,
                                    "SS",
                                    dateOfHire,
                                    birthDate,
                                    true,
                                    organisationUnitDto);
    }

    @DisplayName("Should successfully get all members")
    @Test
    void shouldGetAllMembers() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(member));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expectedDto.id()))
                .andExpect(jsonPath("$[0].name").value(expectedDto.name()))
                .andExpect(jsonPath("$[0].lastName").value(expectedDto.lastName()))
                .andExpect(jsonPath("$[0].employmentState").value(expectedDto.employmentState().toString()))
                .andExpect(jsonPath("$[0].abbreviation").value(expectedDto.abbreviation()))
                .andExpect(jsonPath("$[0].isAdmin").value(expectedDto.isAdmin()))
                .andExpect(jsonPath("$[0].organisationUnit.id").value(expectedDto.organisationUnit().id()))
                .andExpect(jsonPath("$[0].organisationUnit.name").value(expectedDto.organisationUnit().name()));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get member by id")
    @Test
    void shouldGetMemberById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(member);
        BDDMockito.given(mapper.toDto(any(Member.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/" + id).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Susi"))
                .andExpect(jsonPath("$.lastName").value("Sommer"))
                .andExpect(jsonPath("$.employmentState").value("APPLICANT"))
                .andExpect(jsonPath("$.abbreviation").value("SS"))
                .andExpect(jsonPath("$.isAdmin").value(true))
                .andExpect(jsonPath("$.organisationUnit.id").value(1))
                .andExpect(jsonPath("$.organisationUnit.name").value("/bbt"));

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
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Susi"))
                .andExpect(jsonPath("$.lastName").value("Sommer"))
                .andExpect(jsonPath("$.employmentState").value("APPLICANT"))
                .andExpect(jsonPath("$.abbreviation").value("SS"))
                .andExpect(jsonPath("$.isAdmin").value(true))
                .andExpect(jsonPath("$.organisationUnit.id").value(1))
                .andExpect(jsonPath("$.organisationUnit.name").value("/bbt"));

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
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Susi"))
                .andExpect(jsonPath("$.lastName").value("Sommer"))
                .andExpect(jsonPath("$.employmentState").value("APPLICANT"))
                .andExpect(jsonPath("$.abbreviation").value("SS"))
                .andExpect(jsonPath("$.isAdmin").value(true))
                .andExpect(jsonPath("$.organisationUnit.id").value(1))
                .andExpect(jsonPath("$.organisationUnit.name").value("/bbt"));

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
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
