package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceInputDto;
import ch.puzzle.pcts.mapper.LeadershipExperienceMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.LeadershipExperienceBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
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
import tools.jackson.databind.json.JsonMapper;

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
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/leadership-experiences";

    @DisplayName("Should successfully get leadership experience by ID")
    @Test
    void shouldGetLeadershipExperienceById() throws Exception {
        when(businessService.getById(LEADERSHIP_CERT_1_ID)).thenReturn(LEADERSHIP_CERT_1);
        when(mapper.toDto(any(Certificate.class))).thenReturn(LEADERSHIP_CERT_1_DTO);

        mvc
                .perform(get(BASEURL + "/{id}", LEADERSHIP_CERT_1_ID).with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_CERT_1_DTO, "$"));

        verify(businessService, times(1)).getById(LEADERSHIP_CERT_1_ID);
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully create leadership experience")
    @Test
    void shouldCreateLeadershipExperience() throws Exception {
        when(mapper.fromDto(any(LeadershipExperienceInputDto.class))).thenReturn(LEADERSHIP_CERT_1);
        when(businessService.create(any(Certificate.class))).thenReturn(LEADERSHIP_CERT_1);
        when(mapper.toDto(any(Certificate.class))).thenReturn(LEADERSHIP_CERT_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(LEADERSHIP_CERT_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_CERT_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceInputDto.class));
        verify(businessService, times(1)).create(any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully update leadership experience")
    @Test
    void shouldUpdateLeadershipExperience() throws Exception {
        when(mapper.fromDto(any(LeadershipExperienceInputDto.class))).thenReturn(LEADERSHIP_CERT_1);
        when(businessService.update(eq(LEADERSHIP_CERT_1_ID), any(Certificate.class))).thenReturn(LEADERSHIP_CERT_1);
        when(mapper.toDto(any(Certificate.class))).thenReturn(LEADERSHIP_CERT_1_DTO);

        mvc
                .perform(put(BASEURL + "/{id}", LEADERSHIP_CERT_1_ID)
                        .content(jsonMapper.writeValueAsString(LEADERSHIP_CERT_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_CERT_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceInputDto.class));
        verify(businessService, times(1)).update(eq(LEADERSHIP_CERT_1_ID), any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully delete leadership experience")
    @Test
    void shouldDeleteLeadershipExperience() throws Exception {
        doNothing().when(businessService).delete(LEADERSHIP_CERT_1_ID);

        mvc
                .perform(delete(BASEURL + "/{id}", LEADERSHIP_CERT_1_ID).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(businessService, times(1)).delete(LEADERSHIP_CERT_1_ID);
    }
}
