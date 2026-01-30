package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.CALCULATION_1_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.CALCULATION_DTO_1;
import static ch.puzzle.pcts.util.TestDataDTOs.CALCULATION_INPUT_DTO_1;
import static ch.puzzle.pcts.util.TestDataModels.CALCULATION_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.mapper.CalculationMapper;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.business.CalculationBusinessService;
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

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CalculationController.class)
class CalculationControllerIT {

    private static final String BASEURL = "/api/v1/calculations";

    @MockitoBean
    private CalculationBusinessService businessService;
    @MockitoBean
    private CalculationMapper mapper;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private JsonMapper jsonMapper;

    @DisplayName("Should successfully get calculation by ID")
    @Test
    void shouldGetCalculationById() throws Exception {
        when(businessService.getById(CALCULATION_1_ID)).thenReturn(CALCULATION_1);
        when(mapper.toDto(any(Calculation.class))).thenReturn(CALCULATION_DTO_1);

        mvc
                .perform(get(BASEURL + "/{id}", CALCULATION_1_ID).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(CALCULATION_DTO_1, "$"));

        verify(businessService, times(1)).getById(CALCULATION_1_ID);
        verify(mapper, times(1)).toDto(any(Calculation.class));
    }

    @DisplayName("Should successfully create new calculation")
    @Test
    void shouldCreateCalculation() throws Exception {
        when(mapper.fromDto(any(CalculationInputDto.class))).thenReturn(CALCULATION_1);
        when(businessService.create(any(Calculation.class))).thenReturn(CALCULATION_1);
        when(mapper.toDto(any(Calculation.class))).thenReturn(CALCULATION_DTO_1);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(CALCULATION_INPUT_DTO_1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(CALCULATION_DTO_1, "$"));

        verify(mapper, times(1)).fromDto(any(CalculationInputDto.class));
        verify(businessService, times(1)).create(any(Calculation.class));
        verify(mapper, times(1)).toDto(any(Calculation.class));
    }

    @DisplayName("Should successfully update calculation")
    @Test
    void shouldUpdateCalculation() throws Exception {
        when(mapper.fromDto(any(CalculationInputDto.class))).thenReturn(CALCULATION_1);
        when(businessService.update(eq(CALCULATION_1_ID), any(Calculation.class))).thenReturn(CALCULATION_1);
        when(mapper.toDto(any(Calculation.class))).thenReturn(CALCULATION_DTO_1);

        mvc
                .perform(put(BASEURL + "/{id}", CALCULATION_1_ID)
                        .content(jsonMapper.writeValueAsString(CALCULATION_INPUT_DTO_1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(CALCULATION_DTO_1, "$"));

        verify(mapper, times(1)).fromDto(any(CalculationInputDto.class));
        verify(businessService, times(1)).update(eq(CALCULATION_1_ID), any(Calculation.class));
        verify(mapper, times(1)).toDto(any(Calculation.class));
    }
}
