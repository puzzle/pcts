package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.mapper.ExperienceTypeMapper;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.business.ExperienceTypeBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
@WebMvcTest(ExperienceTypeController.class)
class ExperienceTypeControllerIT {

    private static final String BASEURL = "/api/v1/experience-types";
    @MockitoBean
    private ExperienceTypeBusinessService service;
    @MockitoBean
    private ExperienceTypeMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonMapper jsonMapper;
    private ExperienceType experienceType;
    private ExperienceTypeDto requestDto;
    private ExperienceTypeDto expectedDto;
    private Long id;

    @BeforeEach
    void setUp() {
        id = 1L;
        experienceType = new ExperienceType(id,
                                            "ExperienceType 1",
                                            BigDecimal.valueOf(5.3),
                                            BigDecimal.valueOf(4.7),
                                            BigDecimal.valueOf(0));
        requestDto = new ExperienceTypeDto(null,
                                           "ExperienceType 1",
                                           BigDecimal.valueOf(5.3),
                                           BigDecimal.valueOf(4.7),
                                           BigDecimal.valueOf(0));
        expectedDto = new ExperienceTypeDto(id,
                                            "ExperienceType 1",
                                            BigDecimal.valueOf(5.3),
                                            BigDecimal.valueOf(4.7),
                                            BigDecimal.valueOf(0));
    }

    @DisplayName("Should successfully get all experienceTypes")
    @Test
    void shouldGetAllExperienceTypes() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(experienceType));
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

    @DisplayName("Should successfully get experienceType by id")
    @Test
    void shouldGetExperienceTypeById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(experienceType);
        BDDMockito.given(mapper.toDto(any(ExperienceType.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/" + id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getById(1L);
        verify(mapper, times(1)).toDto(any(ExperienceType.class));
    }

    @DisplayName("Should successfully create new experienceType")
    @Test
    void shouldCreateNewExperienceType() throws Exception {
        BDDMockito.given(mapper.fromDto(any(ExperienceTypeDto.class))).willReturn(experienceType);
        BDDMockito.given(service.create(any(ExperienceType.class))).willReturn(experienceType);
        BDDMockito.given(mapper.toDto(any(ExperienceType.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(jsonMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(ExperienceTypeDto.class));
        verify(service, times(1)).create(any(ExperienceType.class));
        verify(mapper, times(1)).toDto(any(ExperienceType.class));
    }

    @DisplayName("Should successfully update experienceType")
    @Test
    void shouldUpdateExperienceType() throws Exception {
        BDDMockito.given(mapper.fromDto(any(ExperienceTypeDto.class))).willReturn(experienceType);
        BDDMockito.given(service.update(any(Long.class), any(ExperienceType.class))).willReturn(experienceType);
        BDDMockito.given(mapper.toDto(any(ExperienceType.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/" + id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(jsonMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(ExperienceTypeDto.class));
        verify(service, times(1)).update(any(Long.class), any(ExperienceType.class));
        verify(mapper, times(1)).toDto(any(ExperienceType.class));
    }

    @DisplayName("Should successfully delete experienceType")
    @Test
    void shouldDeleteExperienceType() throws Exception {
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
