package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceInputDto;
import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.LeadershipExperienceMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LeadershipExperienceController.class)
@ExtendWith(MockitoExtension.class)
@Import(SpringSecurityConfig.class)
class LeadershipExperienceControllerIT {

    @MockitoBean
    private LeadershipExperienceBusinessService businessService;

    @MockitoBean
    private LeadershipExperienceMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASEURL = "/api/v1/leadership-experiences";
    private static final Long ID = 1L;
    private final LocalDate commonDate = LocalDate.of(2019, 8, 4);

    private Certificate certificate;
    private LeadershipExperienceDto dto;
    private LeadershipExperienceInputDto inputDto;

    @BeforeEach
    void setUp() {

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

        CertificateType experienceType = new CertificateType(ID,
                                                             "Leadership Level 1",
                                                             BigDecimal.ONE,
                                                             "Basic level",
                                                             CertificateKind.MILITARY_FUNCTION);

        certificate = Certificate.Builder
                .builder()
                .withId(ID)
                .withMember(member)
                .withCertificateType(experienceType)
                .withComment("Completed via fast-track program")
                .build();

        OrganisationUnitDto orgDto = new OrganisationUnitDto(1L, "/bbt");
        MemberDto memberDto = new MemberDto(ID,
                                            "Susi",
                                            "Miller",
                                            EmploymentState.APPLICANT,
                                            "SM",
                                            commonDate,
                                            commonDate,
                                            orgDto);

        LeadershipExperienceTypeDto experienceTypeDto = new LeadershipExperienceTypeDto(ID,
                                                                                        "Leadership Level 1",
                                                                                        BigDecimal.ONE,
                                                                                        "Basic level",
                                                                                        CertificateKind.MILITARY_FUNCTION);

        dto = new LeadershipExperienceDto(ID, memberDto, experienceTypeDto, "Completed via fast-track program");

        inputDto = new LeadershipExperienceInputDto(ID, ID, "Completed via fast-track program");
    }

    @DisplayName("Should successfully get leadership experience by ID")
    @Test
    void shouldGetLeadershipExperienceById() throws Exception {
        given(businessService.getById(ID)).willReturn(certificate);
        given(mapper.toDto(any(Certificate.class))).willReturn(dto);

        mvc
                .perform(get(BASEURL + "/{id}", ID).with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(dto, "$"));

        verify(businessService, times(1)).getById(ID);
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully create leadership experience")
    @Test
    void shouldCreateLeadershipExperience() throws Exception {
        given(mapper.fromDto(any(LeadershipExperienceInputDto.class))).willReturn(certificate);
        given(businessService.create(any(Certificate.class))).willReturn(certificate);
        given(mapper.toDto(any(Certificate.class))).willReturn(dto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(dto, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceInputDto.class));
        verify(businessService, times(1)).create(any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully update leadership experience")
    @Test
    void shouldUpdateLeadershipExperience() throws Exception {
        given(mapper.fromDto(any(LeadershipExperienceInputDto.class))).willReturn(certificate);
        given(businessService.update(eq(ID), any(Certificate.class))).willReturn(certificate);
        given(mapper.toDto(any(Certificate.class))).willReturn(dto);

        mvc
                .perform(put(BASEURL + "/{id}", ID)
                        .content(objectMapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(dto, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceInputDto.class));
        verify(businessService, times(1)).update(eq(ID), any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully delete leadership experience")
    @Test
    void shouldDeleteLeadershipExperience() throws Exception {
        willDoNothing().given(businessService).delete(ID);

        mvc
                .perform(delete(BASEURL + "/{id}", ID).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(businessService, times(1)).delete(ID);
    }
}
