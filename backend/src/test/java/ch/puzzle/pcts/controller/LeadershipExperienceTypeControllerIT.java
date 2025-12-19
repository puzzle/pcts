package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.LEADERSHIP_TYPE_1_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.LEADERSHIP_TYPE_1_DTO;
import static ch.puzzle.pcts.util.TestDataDTOs.LEADERSHIP_TYPE_1_INPUT;
import static ch.puzzle.pcts.util.TestDataModels.LEADERSHIP_TYPE_1;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.mapper.LeadershipExperienceTypeMapper;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.LeadershipExperienceTypeBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@ControllerIT(LeadershipExperienceTypeController.class)
class LeadershipExperienceTypeControllerIT extends ControllerITBase {

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
        when(service.getAll()).thenReturn(List.of(LEADERSHIP_TYPE_1));
        when(mapper.toDto(any(List.class))).thenReturn(List.of(LEADERSHIP_TYPE_1_DTO));

        mvc
                .perform(get(BASEURL).with(csrf()).with(adminJwt()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_TYPE_1_DTO, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get leadershipExperience type by id")
    @Test
    void shouldGetLeadershipExperienceById() throws Exception {
        when(service.getById(anyLong())).thenReturn(LEADERSHIP_TYPE_1);
        when(mapper.toDto(any(CertificateType.class))).thenReturn(LEADERSHIP_TYPE_1_DTO);

        mvc
                .perform(get(BASEURL + "/1").with(csrf()).with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_TYPE_1_DTO, "$"));

        verify(service, times(1)).getById((1L));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully create new leadershipExperience type")
    @Test
    void shouldCreateNewLeadershipExperience() throws Exception {
        when(mapper.fromDto(any(LeadershipExperienceTypeDto.class))).thenReturn(LEADERSHIP_TYPE_1);
        when(service.create(any(CertificateType.class))).thenReturn(LEADERSHIP_TYPE_1);
        when(mapper.toDto(any(CertificateType.class))).thenReturn(LEADERSHIP_TYPE_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(LEADERSHIP_TYPE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_TYPE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceTypeDto.class));
        verify(service, times(1)).create(any(CertificateType.class));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully update leadershipExperience type")
    @Test
    void shouldUpdateLeadershipExperience() throws Exception {
        when(mapper.fromDto(any(LeadershipExperienceTypeDto.class))).thenReturn(LEADERSHIP_TYPE_1);
        when(service.update(any(Long.class), any(CertificateType.class))).thenReturn(LEADERSHIP_TYPE_1);
        when(mapper.toDto(any(CertificateType.class))).thenReturn(LEADERSHIP_TYPE_1_DTO);

        mvc
                .perform(put(BASEURL + "/" + LEADERSHIP_TYPE_1_ID)
                        .content(jsonMapper.writeValueAsString(LEADERSHIP_TYPE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(LEADERSHIP_TYPE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(LeadershipExperienceTypeDto.class));
        verify(service, times(1)).update(any(Long.class), any(CertificateType.class));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully delete leadershipExperience type")
    @Test
    void shouldDeleteLeadershipExperience() throws Exception {
        doNothing().when(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + LEADERSHIP_TYPE_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
