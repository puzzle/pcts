package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.mapper.DegreeMapper;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.service.business.DegreeBusinessService;
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
@WebMvcTest(DegreeController.class)
class DegreeControllerIT {
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
        BDDMockito.given(businessService.getById(DEGREE_1_ID)).willReturn(DEGREE_1);
        BDDMockito.given(degreeMapper.toDto(any(Degree.class))).willReturn(DEGREE_1_DTO);

        mvc
                .perform(get(BASEURL + "/" + DEGREE_1_ID)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_1_DTO, "$"));

        verify(businessService, times(1)).getById(DEGREE_1_ID);
        verify(mapper, times(1)).toDto(any(Degree.class));
    }

    @DisplayName("Should successfully create degree")
    @Test
    void shouldSuccessfullyCreateDegree() throws Exception {
        BDDMockito.given(degreeMapper.toDto(any(Degree.class))).willReturn(DEGREE_1_DTO);
        BDDMockito.given(businessService.create(any(Degree.class))).willReturn(DEGREE_1);
        BDDMockito.given(degreeMapper.fromDto(any(DegreeInputDto.class))).willReturn(DEGREE_1);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(DEGREE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(DegreeInputDto.class));
        verify(businessService, times(1)).create(any(Degree.class));
        verify(mapper, times(1)).toDto(any(Degree.class));
    }

    @DisplayName("Should successfully update degree")
    @Test
    void shouldSuccessfullyUpdateDegree() throws Exception {
        BDDMockito.given(degreeMapper.toDto(any(Degree.class))).willReturn(DEGREE_1_DTO);
        BDDMockito.given(businessService.update(any(Long.class), any(Degree.class))).willReturn(DEGREE_1);
        BDDMockito.given(degreeMapper.fromDto(any(DegreeInputDto.class))).willReturn(DEGREE_1);

        mvc
                .perform(put(BASEURL + "/" + DEGREE_1_ID)
                        .content(jsonMapper.writeValueAsString(DEGREE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(DEGREE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(DegreeInputDto.class));
        verify(businessService, times(1)).update(any(Long.class), any(Degree.class));
        verify(mapper, times(1)).toDto(any(Degree.class));
    }

    @DisplayName("Should successfully delete degree")
    @Test
    void shouldSuccessfullyDeleteDegree() throws Exception {
        BDDMockito.willDoNothing().given(businessService).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + DEGREE_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(businessService, times(1)).delete(any(Long.class));
    }
}
