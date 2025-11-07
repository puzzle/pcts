package ch.puzzle.pcts.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.membercertificate.MemberCertificateDto;
import ch.puzzle.pcts.dto.membercertificate.MemberCertificateInputDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.MemberCertificateMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.MemberCertificateBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MemberCertificateController.class)
class MemberCertificateControllerIT {

    @MockitoBean
    private MemberCertificateBusinessService service;

    @MockitoBean
    private MemberCertificateMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASEURL = "/api/v1/member_certificates";
    private static final Long ID = 1L;
    private static final Date COMMON_DATE = new Date(0L);

    private MemberCertificate memberCertificate;
    private MemberCertificateInputDto requestDto;
    private MemberCertificateDto expectedDto;

    @BeforeEach
    void setUp() {
        Set<Tag> tags = Set.of(new Tag(1L, "Tag 1"));

        Certificate certificate = new Certificate(ID, "Certificate", new BigDecimal("5.5"), "Comment", tags);
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "/bbt");

        Member member = Member.Builder
                .builder()
                .withId(ID)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(COMMON_DATE)
                .withBirthDate(COMMON_DATE)
                .withOrganisationUnit(organisationUnit)
                .build();

        memberCertificate = MemberCertificate.Builder
                .builder()
                .withId(ID)
                .withMember(member)
                .withCertificate(certificate)
                .withCompleted_at(COMMON_DATE)
                .withComment("Comment")
                .build();

        requestDto = new MemberCertificateInputDto(ID, ID, COMMON_DATE, COMMON_DATE, "Comment");

        OrganisationUnitDto organisationUnitDto = new OrganisationUnitDto(1L, "/bbt");
        MemberDto memberDto = new MemberDto(ID,
                                            "Susi",
                                            "Miller",
                                            EmploymentState.APPLICANT,
                                            "SM",
                                            COMMON_DATE,
                                            COMMON_DATE,
                                            organisationUnitDto);
        CertificateDto certificateDto = new CertificateDto(ID,
                                                           "CertificateDto",
                                                           new BigDecimal("5.5"),
                                                           "Comment",
                                                           tags.stream().map(Tag::getName).toList());

        expectedDto = new MemberCertificateDto(ID, memberDto, certificateDto, COMMON_DATE, COMMON_DATE, "Comment");
    }

    @DisplayName("Should successfully get all member certificate")
    @Test
    void shouldGetAllMemberCertificate() throws Exception {
        given(service.getAll()).willReturn(List.of(memberCertificate));
        given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        ResultActions result = mvc
                .perform(get(BASEURL).with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        expectMemberCertificate(result, "$[0]", expectedDto);

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get member certificate by id")
    @Test
    void shouldGetMemberCertificateById() throws Exception {
        given(service.getById(eq(ID))).willReturn(memberCertificate);
        given(mapper.toDto(any(MemberCertificate.class))).willReturn(expectedDto);

        ResultActions result = mvc.perform(get(BASEURL + "/{id}", ID).with(csrf())).andExpect(status().isOk());

        expectMemberCertificate(result, "$", expectedDto);

        verify(service, times(1)).getById(ID);
        verify(mapper, times(1)).toDto(any(MemberCertificate.class));
    }

    @DisplayName("Should successfully create new member certificate")
    @Test
    void shouldCreateNewMemberCertificate() throws Exception {
        given(mapper.fromDto(any(MemberCertificateInputDto.class))).willReturn(memberCertificate);
        given(service.create(any(MemberCertificate.class))).willReturn(memberCertificate);
        given(mapper.toDto(any(MemberCertificate.class))).willReturn(expectedDto);

        ResultActions result = mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated());

        expectMemberCertificate(result, "$", expectedDto);

        verify(mapper, times(1)).fromDto(any(MemberCertificateInputDto.class));
        verify(service, times(1)).create(any(MemberCertificate.class));
        verify(mapper, times(1)).toDto(any(MemberCertificate.class));
    }

    @DisplayName("Should successfully update member certificate")
    @Test
    void shouldUpdateMemberCertificate() throws Exception {
        given(mapper.fromDto(any(MemberCertificateInputDto.class))).willReturn(memberCertificate);
        given(service.update(eq(ID), any(MemberCertificate.class))).willReturn(memberCertificate);
        given(mapper.toDto(any(MemberCertificate.class))).willReturn(expectedDto);

        ResultActions result = mvc
                .perform(put(BASEURL + "/{id}", ID)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());

        expectMemberCertificate(result, "$", expectedDto);

        verify(mapper, times(1)).fromDto(any(MemberCertificateInputDto.class));
        verify(service, times(1)).update(eq(ID), any(MemberCertificate.class));
        verify(mapper, times(1)).toDto(any(MemberCertificate.class));
    }

    @DisplayName("Should successfully delete member certificate")
    @Test
    void shouldDeleteMemberCertificate() throws Exception {
        willDoNothing().given(service).delete(ID);

        mvc
                .perform(delete(BASEURL + "/{id}", ID).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(ID);
    }

    private void expectMemberCertificate(ResultActions result, String path, MemberCertificateDto dto) throws Exception {
        result
                .andExpect(jsonPath(path + ".id").value(dto.id()))
                .andExpect(jsonPath(path + ".comment").value(dto.comment()))
                .andExpect(jsonPath(path + ".member.id").value(dto.member().id()))
                .andExpect(jsonPath(path + ".member.firstName").value(dto.member().firstName()))
                .andExpect(jsonPath(path + ".member.lastName").value(dto.member().lastName()))
                .andExpect(jsonPath(path + ".member.employmentState").value(dto.member().employmentState().toString()))
                .andExpect(jsonPath(path + ".member.abbreviation").value(dto.member().abbreviation()))
                .andExpect(jsonPath(path + ".member.organisationUnit.id").value(dto.member().organisationUnit().id()))
                .andExpect(jsonPath(path + ".member.organisationUnit.name")
                        .value(dto.member().organisationUnit().name()))
                .andExpect(jsonPath(path + ".certificate.id").value(dto.certificate().id()))
                .andExpect(jsonPath(path + ".certificate.name").value(dto.certificate().name()))
                .andExpect(jsonPath(path + ".certificate.points").value(dto.certificate().points()))
                .andExpect(jsonPath(path + ".certificate.tags.length()").value(dto.certificate().tags().size()))
                .andExpect(jsonPath(path + ".certificate.tags", containsInAnyOrder(dto.certificate().tags().toArray())))
                .andExpect(jsonPath(path + ".certificate.comment").value(dto.certificate().comment()));
    }
}