package ch.puzzle.pcts.controller;

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
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.CertificateMapper;
import ch.puzzle.pcts.mapper.ErrorMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
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

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CertificateController.class)
class CertificateControllerIT {

    @MockitoBean
    private ErrorMapper errorMapper;

    @MockitoBean
    private CertificateBusinessService service;

    @MockitoBean
    private CertificateMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASEURL = "/api/v1/certificates";
    private static final Long ID = 1L;
    private final LocalDate commonDate = LocalDate.of(2019, 8, 4);

    private Certificate certificate;
    private CertificateInputDto requestDto;
    private CertificateDto expectedDto;

    @BeforeEach
    void setUp() {
        Set<Tag> tags = Set.of(new Tag(1L, "Tag 1"));

        CertificateType certificateType = new CertificateType(ID,
                                                              "Certificate",
                                                              new BigDecimal("5.5"),
                                                              "Comment",
                                                              tags);
        OrganisationUnit organisationUnit = new OrganisationUnit(1L, "/bbt");

        Member member = Member.Builder
                .builder()
                .withId(ID)
                .withFirstName("Susi")
                .withLastName("Miller")
                .withEmploymentState(EmploymentState.APPLICANT)
                .withAbbreviation("SM")
                .withDateOfHire(commonDate)
                .withBirthDate(commonDate)
                .withOrganisationUnit(organisationUnit)
                .build();

        certificate = Certificate.Builder
                .builder()
                .withId(ID)
                .withMember(member)
                .withCertificateType(certificateType)
                .withCompletedAt(commonDate)
                .withComment("Comment")
                .build();

        requestDto = new CertificateInputDto(ID, ID, commonDate, commonDate, "Comment");

        OrganisationUnitDto organisationUnitDto = new OrganisationUnitDto(1L, "/bbt");
        MemberDto memberDto = new MemberDto(ID,
                                            "Susi",
                                            "Miller",
                                            EmploymentState.APPLICANT,
                                            "SM",
                                            commonDate,
                                            commonDate,
                                            organisationUnitDto);
        CertificateTypeDto certificateDto = new CertificateTypeDto(ID,
                                                                   "CertificateDto",
                                                                   new BigDecimal("5.5"),
                                                                   "Comment",
                                                                   tags.stream().map(Tag::getName).toList());

        expectedDto = new CertificateDto(ID, memberDto, certificateDto, commonDate, commonDate, "Comment");
    }

    @DisplayName("Should successfully get all certificate")
    @Test
    void shouldGetAllMemberCertificate() throws Exception {
        given(service.getAll()).willReturn(List.of(certificate));
        given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        mvc
                .perform(get(BASEURL).with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get certificate by id")
    @Test
    void shouldGetCertificateById() throws Exception {
        given(service.getById(ID)).willReturn(certificate);
        given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/{id}", ID).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getById(ID);
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully create new certificate")
    @Test
    void shouldCreateNewMemberCertificate() throws Exception {
        given(mapper.fromDto(any(CertificateInputDto.class))).willReturn(certificate);
        given(service.create(any(Certificate.class))).willReturn(certificate);
        given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(CertificateInputDto.class));
        verify(service, times(1)).create(any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully update certificate")
    @Test
    void shouldUpdateCertificate() throws Exception {
        given(mapper.fromDto(any(CertificateInputDto.class))).willReturn(certificate);
        given(service.update(eq(ID), any(Certificate.class))).willReturn(certificate);
        given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/{id}", ID)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(CertificateInputDto.class));
        verify(service, times(1)).update(eq(ID), any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully delete certificate")
    @Test
    void shouldDeleteCertificate() throws Exception {
        willDoNothing().given(service).delete(ID);

        mvc
                .perform(delete(BASEURL + "/{id}", ID).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(ID);
    }
}