package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.DEGREE_TYPE_1_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.DEGREE_TYPE_1_DTO;
import static ch.puzzle.pcts.util.TestDataDTOs.DEGREE_TYPE_1_INPUT;
import static ch.puzzle.pcts.util.TestDataModels.DEGREE_TYPE_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.mapper.DegreeTypeMapper;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.service.business.DegreeTypeBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@ControllerIT(DegreeTypeController.class)
class DegreeTypeControllerIT extends ControllerITBase {

    @MockitoBean
    private DegreeTypeBusinessService service;

    @MockitoBean
    private DegreeTypeMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/degree-types";

    @DisplayName("Should successfully get all degree types")
    @Test
    void shouldGetAllDegreeTypes() throws Exception {
        when(service.getAll()).thenReturn(List.of(DEGREE_TYPE_1));
        when(mapper.toDto(any(List.class))).thenReturn(List.of(DEGREE_TYPE_1_DTO));

        mvc
                .perform(get(BASEURL).with(csrf()).with(adminJwt()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_TYPE_1_DTO, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get degree type by id")
    @Test
    void shouldGetDegreeTypeById() throws Exception {
        when(service.getById(anyLong())).thenReturn(DEGREE_TYPE_1);
        when(mapper.toDto(any(DegreeType.class))).thenReturn(DEGREE_TYPE_1_DTO);

        mvc
                .perform(get(BASEURL + "/1").with(csrf()).with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_TYPE_1_DTO, "$"));

        verify(service, times(1)).getById(1L);
        verify(mapper, times(1)).toDto(any(DegreeType.class));
    }

    @DisplayName("Should successfully create new degree type")
    @Test
    void shouldCreateNewDegreeType() throws Exception {
        when(mapper.fromDto(any(DegreeTypeDto.class))).thenReturn(DEGREE_TYPE_1);
        when(service.create(any(DegreeType.class))).thenReturn(DEGREE_TYPE_1);
        when(mapper.toDto(any(DegreeType.class))).thenReturn(DEGREE_TYPE_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(DEGREE_TYPE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_TYPE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(DegreeTypeDto.class));
        verify(service, times(1)).create(any(DegreeType.class));
        verify(mapper, times(1)).toDto(any(DegreeType.class));
    }

    @DisplayName("Should successfully update Degree type")
    @Test
    void shouldUpdateDegreeType() throws Exception {
        when(mapper.fromDto(any(DegreeTypeDto.class))).thenReturn(DEGREE_TYPE_1);
        when(service.update(any(Long.class), any(DegreeType.class))).thenReturn(DEGREE_TYPE_1);
        when(mapper.toDto(any(DegreeType.class))).thenReturn(DEGREE_TYPE_1_DTO);

        mvc
                .perform(put(BASEURL + "/" + DEGREE_TYPE_1_ID)
                        .content(jsonMapper.writeValueAsString(DEGREE_TYPE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_TYPE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(DegreeTypeDto.class));
        verify(service, times(1)).update(any(Long.class), any(DegreeType.class));
        verify(mapper, times(1)).toDto(any(DegreeType.class));
    }

    @DisplayName("Should successfully delete degree type")
    @Test
    void shouldDeleteDegreeType() throws Exception {
        doNothing().when(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + DEGREE_TYPE_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
