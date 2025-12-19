package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.EXP_TYPE_1_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.EXP_TYPE_1_DTO;
import static ch.puzzle.pcts.util.TestDataDTOs.EXP_TYPE_1_INPUT;
import static ch.puzzle.pcts.util.TestDataModels.EXP_TYPE_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.mapper.ExperienceTypeMapper;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.business.ExperienceTypeBusinessService;
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

@ControllerIT(ExperienceTypeController.class)
class ExperienceTypeControllerIT extends ControllerITBase {

    private static final String BASEURL = "/api/v1/experience-types";
    @MockitoBean
    private ExperienceTypeBusinessService service;
    @MockitoBean
    private ExperienceTypeMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonMapper jsonMapper;

    @DisplayName("Should successfully get all experienceTypes")
    @Test
    void shouldGetAllExperienceTypes() throws Exception {
        when(service.getAll()).thenReturn(List.of(EXP_TYPE_1));
        when(mapper.toDto(any(List.class))).thenReturn(List.of(EXP_TYPE_1_DTO));
        mvc
                .perform(get(BASEURL).with(csrf()).with(adminJwt()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(EXP_TYPE_1_DTO, "$[0]"));
        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get experienceType by id")
    @Test
    void shouldGetExperienceTypeById() throws Exception {
        when(service.getById(anyLong())).thenReturn(EXP_TYPE_1);
        when(mapper.toDto(any(ExperienceType.class))).thenReturn(EXP_TYPE_1_DTO);

        mvc
                .perform(get(BASEURL + "/" + EXP_TYPE_1_ID).with(csrf()).with(adminJwt()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(EXP_TYPE_1, "$"));

        verify(service, times(1)).getById(1L);
        verify(mapper, times(1)).toDto(any(ExperienceType.class));
    }

    @DisplayName("Should successfully create new experienceType")
    @Test
    void shouldCreateNewExperienceType() throws Exception {
        when(mapper.fromDto(any(ExperienceTypeDto.class))).thenReturn(EXP_TYPE_1);
        when(service.create(any(ExperienceType.class))).thenReturn(EXP_TYPE_1);
        when(mapper.toDto(any(ExperienceType.class))).thenReturn(EXP_TYPE_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .with(csrf())
                        .with(adminJwt())
                        .content(jsonMapper.writeValueAsString(EXP_TYPE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(EXP_TYPE_1, "$"));

        verify(mapper, times(1)).fromDto(any(ExperienceTypeDto.class));
        verify(service, times(1)).create(any(ExperienceType.class));
        verify(mapper, times(1)).toDto(any(ExperienceType.class));
    }

    @DisplayName("Should successfully update experienceType")
    @Test
    void shouldUpdateExperienceType() throws Exception {
        when(mapper.fromDto(any(ExperienceTypeDto.class))).thenReturn(EXP_TYPE_1);
        when(service.update(any(Long.class), any(ExperienceType.class))).thenReturn(EXP_TYPE_1);
        when(mapper.toDto(any(ExperienceType.class))).thenReturn(EXP_TYPE_1_DTO);

        mvc
                .perform(put(BASEURL + "/" + EXP_TYPE_1_ID)
                        .with(csrf())
                        .with(adminJwt())
                        .content(jsonMapper.writeValueAsString(EXP_TYPE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(EXP_TYPE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(ExperienceTypeDto.class));
        verify(service, times(1)).update(any(Long.class), any(ExperienceType.class));
        verify(mapper, times(1)).toDto(any(ExperienceType.class));
    }

    @DisplayName("Should successfully delete experienceType")
    @Test
    void shouldDeleteExperienceType() throws Exception {
        doNothing().when(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + EXP_TYPE_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
