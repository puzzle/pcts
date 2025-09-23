package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.degree_type.DegreeTypeDto;
import ch.puzzle.pcts.dto.degree_type.DegreeTypeNameDto;
import ch.puzzle.pcts.dto.degree_type.DegreeTypeWithoutIdDto;
import ch.puzzle.pcts.mapper.DegreeTypeMapper;
import ch.puzzle.pcts.model.degree_type.DegreeType;
import ch.puzzle.pcts.service.business.DegreeTypeBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@Import(SpringSecurityConfig.class)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(DegreeTypeController.class)
public class DegreeTypeControllerIT {

    @MockitoBean
    private DegreeTypeBusinessService service;

    @MockitoBean
    private DegreeTypeMapper mapper;

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASEURL = "/api/v1/degree-types";

    private DegreeType degreeType;
    private DegreeTypeNameDto degreeTypeNameDto;
    private DegreeTypeWithoutIdDto requestDto;
    private DegreeTypeDto expectedDto;
    private Long degreeTypeId;

    @BeforeEach
    void setUp() {
        degreeType = new DegreeType(1L,
                                    "Degree type 1",
                                    new BigDecimal("1.0"),
                                    new BigDecimal("2.0"),
                                    new BigDecimal("3.0"));
        degreeTypeNameDto = new DegreeTypeNameDto(1L, "Degree type 1");
        requestDto = new DegreeTypeWithoutIdDto("Degree type 1",
                                                new BigDecimal("1.0"),
                                                new BigDecimal("2.0"),
                                                new BigDecimal("3.0"));
        expectedDto = new DegreeTypeDto(1L,
                                        "Degree type 1",
                                        new BigDecimal("1.0"),
                                        new BigDecimal("2.0"),
                                        new BigDecimal("3.0"));
        degreeTypeId = 1L;
    }

    @DisplayName("Should successfully get all degree types")
    @Test
    void shouldGetAllDegreeTypes() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(degreeType));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].degreeTypeId").value(expectedDto.degreeTypeId()))
                .andExpect(jsonPath("$[0].name").value(expectedDto.name()))
                .andExpect(jsonPath("$[0].highlyRelevantPoints").value(expectedDto.highlyRelevantPoints()))
                .andExpect(jsonPath("$[0].limitedRelevantPoints").value(expectedDto.limitedRelevantPoints()))
                .andExpect(jsonPath("$[0].littleRelevantPoints").value(expectedDto.littleRelevantPoints()));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get degree type by id")
    @Test
    void shouldGetDegreeTypeById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(degreeType);
        BDDMockito.given(mapper.toDto(any(DegreeType.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.degreeTypeId").isNumber())
                .andExpect(jsonPath("$.highlyRelevantPoints").value(new BigDecimal("1.0")))
                .andExpect(jsonPath("$.limitedRelevantPoints").value(new BigDecimal("2.0")))
                .andExpect(jsonPath("$.littleRelevantPoints").value(new BigDecimal("3.0")));

        verify(service, times(1)).getById(eq(1L));
        verify(mapper, times(1)).toDto(any(DegreeType.class));
    }

    @DisplayName("Should successfully get all degree types names by id")
    @Test
    void shouldGetAllDegreeTypesNamesById() throws Exception {
        BDDMockito.given(service.getAllNames()).willReturn(List.of(degreeTypeNameDto));

        mvc
                .perform(get(BASEURL + "/names")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].degreeTypeId").value(expectedDto.degreeTypeId()))
                .andExpect(jsonPath("$[0].name").value(expectedDto.name()));

        verify(service, times(1)).getAllNames();
    }

    @DisplayName("Should successfully create new degree type")
    @Test
    void shouldCreateNewDegreeType() throws Exception {
        BDDMockito.given(mapper.fromDto(any(DegreeTypeWithoutIdDto.class))).willReturn(degreeType);
        BDDMockito.given(service.create(any(DegreeType.class))).willReturn(degreeType);
        BDDMockito.given(mapper.toDto(any(DegreeType.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.degreeTypeId").isNumber())
                .andExpect(jsonPath("$.highlyRelevantPoints").value(new BigDecimal("1.0")))
                .andExpect(jsonPath("$.limitedRelevantPoints").value(new BigDecimal("2.0")))
                .andExpect(jsonPath("$.littleRelevantPoints").value(new BigDecimal("3.0")));

        verify(mapper, times(1)).fromDto(any(DegreeTypeWithoutIdDto.class));
        verify(service, times(1)).create(any(DegreeType.class));
        verify(mapper, times(1)).toDto(any(DegreeType.class));
    }

    @DisplayName("Should successfully update Degree type")
    @Test
    void shouldUpdateDegreeType() throws Exception {
        BDDMockito.given(mapper.fromDto(any(DegreeTypeWithoutIdDto.class))).willReturn(degreeType);
        BDDMockito.given(service.update(any(Long.class), any(DegreeType.class))).willReturn(degreeType);
        BDDMockito.given(mapper.toDto(any(DegreeType.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/" + degreeTypeId)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.degreeTypeId").isNumber())
                .andExpect(jsonPath("$.highlyRelevantPoints").value(new BigDecimal("1.0")))
                .andExpect(jsonPath("$.limitedRelevantPoints").value(new BigDecimal("2.0")))
                .andExpect(jsonPath("$.littleRelevantPoints").value(new BigDecimal("3.0")));

        verify(mapper, times(1)).fromDto(any(DegreeTypeWithoutIdDto.class));
        verify(service, times(1)).update(any(Long.class), any(DegreeType.class));
        verify(mapper, times(1)).toDto(any(DegreeType.class));
    }

    @DisplayName("Should successfully delete degree type")
    @Test
    void shouldDeleteDegreeType() throws Exception {
        BDDMockito.willDoNothing().given(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + degreeTypeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
