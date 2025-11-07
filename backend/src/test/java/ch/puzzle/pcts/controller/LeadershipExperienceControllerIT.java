package ch.puzzle.pcts.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.leadership_experience.LeadershipExperienceDto;
import ch.puzzle.pcts.mapper.LeadershipExperienceMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
@WebMvcTest(LeadershipExperienceController.class)
class LeadershipExperienceControllerIT {

    @MockitoBean
    private LeadershipExperienceBusinessService service;

    @MockitoBean
    private LeadershipExperienceMapper mapper;

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASEURL = "/api/v1/leadership-experiences";

    private Certificate leadershipExperience;
    private LeadershipExperienceDto requestDto;
    private LeadershipExperienceDto expectedDto;
    private Long id;

    @BeforeEach
    void setUp() {
        leadershipExperience = new Certificate(1L,
                                               "LeadershipExperience 1",
                                               new BigDecimal("5.5"),
                                               "This is LeadershipExperience 1",
                                               CertificateType.LEADERSHIP_TRAINING);
        requestDto = new LeadershipExperienceDto(null,
                                                 "LeadershipExperience 1",
                                                 new BigDecimal("5.5"),
                                                 "This is LeadershipExperience 1",
                                                 CertificateType.LEADERSHIP_TRAINING);
        expectedDto = new LeadershipExperienceDto(1L,
                                                  "LeadershipExperience 1",
                                                  new BigDecimal("5.5"),
                                                  "This is LeadershipExperience 1",
                                                  CertificateType.LEADERSHIP_TRAINING);
        id = 1L;
    }

    @DisplayName("Should successfully get all leadershipExperiences")
    @Test
    void shouldGetAllLeadershipExperiences() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(leadershipExperience));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get leadershipExperience by id")
    @Test
    void shouldGetLeadershipExperienceById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(leadershipExperience);
        BDDMockito.given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getById((1L));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully create new leadershipExperience")
    @Test
    void shouldCreateNewLeadershipExperience() throws Exception {
        BDDMockito.given(mapper.fromDto(any(LeadershipExperienceDto.class))).willReturn(leadershipExperience);
        BDDMockito.given(service.create(any(Certificate.class))).willReturn(leadershipExperience);
        BDDMockito.given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceDto.class));
        verify(service, times(1)).create(any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully update leadershipExperience")
    @Test
    void shouldUpdateLeadershipExperience() throws Exception {
        BDDMockito.given(mapper.fromDto(any(LeadershipExperienceDto.class))).willReturn(leadershipExperience);
        BDDMockito.given(service.update(any(Long.class), any(Certificate.class))).willReturn(leadershipExperience);
        BDDMockito.given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceDto.class));
        verify(service, times(1)).update(any(Long.class), any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully delete leadershipExperience")
    @Test
    void shouldDeleteLeadershipExperience() throws Exception {
        BDDMockito.willDoNothing().given(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
