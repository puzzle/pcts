package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.mockito.BDDMockito;
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
        BDDMockito.given(service.getAll()).willReturn(List.of(DEGREE_TYPE_1));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(DEGREE_TYPE_1_DTO));

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
        BDDMockito.given(service.getById(anyLong())).willReturn(DEGREE_TYPE_1);
        BDDMockito.given(mapper.toDto(any(DegreeType.class))).willReturn(DEGREE_TYPE_1_DTO);

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
        BDDMockito.given(mapper.fromDto(any(DegreeTypeDto.class))).willReturn(DEGREE_TYPE_1);
        BDDMockito.given(service.create(any(DegreeType.class))).willReturn(DEGREE_TYPE_1);
        BDDMockito.given(mapper.toDto(any(DegreeType.class))).willReturn(DEGREE_TYPE_1_DTO);

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
        BDDMockito.given(mapper.fromDto(any(DegreeTypeDto.class))).willReturn(DEGREE_TYPE_1);
        BDDMockito.given(service.update(any(Long.class), any(DegreeType.class))).willReturn(DEGREE_TYPE_1);
        BDDMockito.given(mapper.toDto(any(DegreeType.class))).willReturn(DEGREE_TYPE_1_DTO);

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
        BDDMockito.willDoNothing().given(service).delete(anyLong());

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
