package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.mapper.LeadershipExperienceTypeMapper;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.LeadershipExperienceTypeBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(LeadershipExperienceTypeController.class)
class LeadershipExperienceTypeControllerIT {

    @MockitoBean
    private LeadershipExperienceTypeBusinessService service;

    @MockitoBean
    private LeadershipExperienceTypeMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/leadership-experience-types";

    @DisplayName("Should successfully get all leadershipExperience types")
    @Test
    void shouldGetAllLeadershipExperienceTypes() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(LEADERSHIP_TYPE_1));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(LEADERSHIP_TYPE_1_DTO));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_TYPE_1_DTO, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get leadershipExperience type by id")
    @Test
    void shouldGetLeadershipExperienceById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(LEADERSHIP_TYPE_1);
        BDDMockito.given(mapper.toDto(any(CertificateType.class))).willReturn(LEADERSHIP_TYPE_1_DTO);

        mvc
                .perform(get(BASEURL + "/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_TYPE_1_DTO, "$"));

        verify(service, times(1)).getById((1L));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully create new leadershipExperience type")
    @Test
    void shouldCreateNewLeadershipExperience() throws Exception {
        BDDMockito.given(mapper.fromDto(any(LeadershipExperienceTypeDto.class))).willReturn(LEADERSHIP_TYPE_1);
        BDDMockito.given(service.create(any(CertificateType.class))).willReturn(LEADERSHIP_TYPE_1);
        BDDMockito.given(mapper.toDto(any(CertificateType.class))).willReturn(LEADERSHIP_TYPE_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(LEADERSHIP_TYPE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_TYPE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceTypeDto.class));
        verify(service, times(1)).create(any(CertificateType.class));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully update leadershipExperience type")
    @Test
    void shouldUpdateLeadershipExperience() throws Exception {
        BDDMockito.given(mapper.fromDto(any(LeadershipExperienceTypeDto.class))).willReturn(LEADERSHIP_TYPE_1);
        BDDMockito.given(service.update(any(Long.class), any(CertificateType.class))).willReturn(LEADERSHIP_TYPE_1);
        BDDMockito.given(mapper.toDto(any(CertificateType.class))).willReturn(LEADERSHIP_TYPE_1_DTO);

        mvc
                .perform(put(BASEURL + "/" + LEADERSHIP_TYPE_1_ID)
                        .content(jsonMapper.writeValueAsString(LEADERSHIP_TYPE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_TYPE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceTypeDto.class));
        verify(service, times(1)).update(any(Long.class), any(CertificateType.class));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully delete leadershipExperience type")
    @Test
    void shouldDeleteLeadershipExperience() throws Exception {
        BDDMockito.willDoNothing().given(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + LEADERSHIP_TYPE_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
