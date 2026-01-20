package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.DEGREE_1_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.DEGREE_1_DTO;
import static ch.puzzle.pcts.util.TestDataDTOs.DEGREE_1_INPUT;
import static ch.puzzle.pcts.util.TestDataModels.DEGREE_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.mapper.DegreeMapper;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.service.business.DegreeBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@ControllerIT(DegreeController.class)
class DegreeControllerIT extends ControllerITBase {
    @MockitoBean
    private DegreeBusinessService businessService;

    @MockitoBean
    private DegreeMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/degrees";

    @Autowired
    private DegreeMapper degreeMapper;

    @DisplayName("Should successfully get degree by id")
    @Test
    void shouldSuccessfullyGetDegreeById() throws Exception {
        when(businessService.getById(DEGREE_1_ID)).thenReturn(DEGREE_1);
        when(degreeMapper.toDto(any(Degree.class))).thenReturn(DEGREE_1_DTO);

        mvc
                .perform(get(BASEURL + "/" + DEGREE_1_ID)
                        .with(csrf())
                        .with(adminJwt())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_1_DTO, "$"));

        verify(businessService, times(1)).getById(DEGREE_1_ID);
        verify(mapper, times(1)).toDto(any(Degree.class));
    }

    @DisplayName("Should successfully create degree")
    @Test
    void shouldSuccessfullyCreateDegree() throws Exception {
        when(degreeMapper.toDto(any(Degree.class))).thenReturn(DEGREE_1_DTO);
        when(businessService.create(any(Degree.class))).thenReturn(DEGREE_1);
        when(degreeMapper.fromDto(any(DegreeInputDto.class))).thenReturn(DEGREE_1);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(DEGREE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(DegreeInputDto.class));
        verify(businessService, times(1)).create(any(Degree.class));
        verify(mapper, times(1)).toDto(any(Degree.class));
    }

    @DisplayName("Should successfully update degree")
    @Test
    void shouldSuccessfullyUpdateDegree() throws Exception {
        when(degreeMapper.toDto(any(Degree.class))).thenReturn(DEGREE_1_DTO);
        when(businessService.update(any(Long.class), any(Degree.class))).thenReturn(DEGREE_1);
        when(degreeMapper.fromDto(any(DegreeInputDto.class))).thenReturn(DEGREE_1);

        mvc
                .perform(put(BASEURL + "/" + DEGREE_1_ID)
                        .content(jsonMapper.writeValueAsString(DEGREE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(DegreeInputDto.class));
        verify(businessService, times(1)).update(any(Long.class), any(Degree.class));
        verify(mapper, times(1)).toDto(any(Degree.class));
    }

    @DisplayName("Should successfully delete degree")
    @Test
    void shouldSuccessfullyDeleteDegree() throws Exception {
        doNothing().when(businessService).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + DEGREE_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isNoContent());

        verify(businessService, times(1)).delete(any(Long.class));
    }
}
