package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.experience.ExperienceInputDto;
import ch.puzzle.pcts.mapper.ExperienceMapper;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.service.business.ExperienceBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
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
@WebMvcTest(ExperienceController.class)
class ExperienceControllerIT {

    private static final String BASEURL = "/api/v1/experiences";
    @MockitoBean
    private ExperienceBusinessService service;
    @MockitoBean
    private ExperienceMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonMapper jsonMapper;

    @DisplayName("Should successfully get experience by id")
    @Test
    void shouldGetExperienceById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(EXPERIENCE_1);
        BDDMockito.given(mapper.toDto(any(Experience.class))).willReturn(EXPERIENCE_1_DTO);

        mvc
                .perform(get(BASEURL + "/" + EXPERIENCE_1_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(EXPERIENCE_1_DTO, "$"));

        verify(service, times(1)).getById(EXPERIENCE_1_ID);
        verify(mapper, times(1)).toDto(any(Experience.class));
    }

    @DisplayName("Should successfully create new experience")
    @Test
    void shouldCreateExperience() throws Exception {
        BDDMockito.given(mapper.fromDto(any(ExperienceInputDto.class))).willReturn(EXPERIENCE_1);
        BDDMockito.given(service.create(any(Experience.class))).willReturn(EXPERIENCE_1);
        BDDMockito.given(mapper.toDto(any(Experience.class))).willReturn(EXPERIENCE_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(EXPERIENCE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(EXPERIENCE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(ExperienceInputDto.class));
        verify(service, times(1)).create(any(Experience.class));
        verify(mapper, times(1)).toDto(any(Experience.class));
    }

    @DisplayName("Should successfully update experience")
    @Test
    void shouldUpdateExperience() throws Exception {
        BDDMockito.given(mapper.fromDto(any(ExperienceInputDto.class))).willReturn(EXPERIENCE_1);
        BDDMockito.given(service.update(anyLong(), any(Experience.class))).willReturn(EXPERIENCE_1);
        BDDMockito.given(mapper.toDto(any(Experience.class))).willReturn(EXPERIENCE_1_DTO);

        mvc
                .perform(put(BASEURL + "/" + EXPERIENCE_1_ID)
                        .content(jsonMapper.writeValueAsString(EXPERIENCE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(EXPERIENCE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(ExperienceInputDto.class));
        verify(service, times(1)).update(anyLong(), any(Experience.class));
        verify(mapper, times(1)).toDto(any(Experience.class));
    }

    @DisplayName("Should successfully delete experience")
    @Test
    void shouldDeleteExperience() throws Exception {
        BDDMockito.willDoNothing().given(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + EXPERIENCE_1_ID).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(anyLong());
    }
}
