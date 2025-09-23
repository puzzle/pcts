package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.organisation_unit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.OrganisationUnitMapper;
import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@WebMvcTest(OrganisationUnitController.class)
class OrganisationUnitControllerIT {

    @MockitoBean
    private OrganisationUnitBusinessService service;

    @MockitoBean
    private OrganisationUnitMapper mapper;

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASEURL = "/api/v1/organisation-units";

    private OrganisationUnit organisationUnit;
    private OrganisationUnitDto requestDto;
    private OrganisationUnitDto expectedDto;
    private Long id;

    @BeforeEach
    void setUp() {
        organisationUnit = new OrganisationUnit(1L, "OrganisationUnit 1");
        requestDto = new OrganisationUnitDto(null, "OrganisationUnit 1");
        expectedDto = new OrganisationUnitDto(1L, "OrganisationUnit 1");
        id = 1L;
    }

    @DisplayName("Should successfully get all roles organisation units")
    @Test
    void shouldGetAllOrganisationUnits() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(organisationUnit));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expectedDto.id()))
                .andExpect(jsonPath("$[0].name").value(expectedDto.name()));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get organisation unit by id")
    @Test
    void shouldGetOrganisationUnitById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(organisationUnit);
        BDDMockito.given(mapper.toDto(any(OrganisationUnit.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("OrganisationUnit 1"));

        verify(service, times(1)).getById(1L);
        verify(mapper, times(1)).toDto(any(OrganisationUnit.class));
    }

    @DisplayName("Should successfully create new organisation unit")
    @Test
    void shouldCreateNewOrganisationUnit() throws Exception {
        BDDMockito.given(mapper.fromDto(any(OrganisationUnitDto.class))).willReturn(organisationUnit);
        BDDMockito.given(service.create(any(OrganisationUnit.class))).willReturn(organisationUnit);
        BDDMockito.given(mapper.toDto(any(OrganisationUnit.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("OrganisationUnit 1"));

        verify(mapper, times(1)).fromDto(any(OrganisationUnitDto.class));
        verify(service, times(1)).create(any(OrganisationUnit.class));
        verify(mapper, times(1)).toDto(any(OrganisationUnit.class));
    }

    @DisplayName("Should successfully update organisation unit")
    @Test
    void shouldUpdateOrganisationUnit() throws Exception {
        BDDMockito.given(mapper.fromDto(any(OrganisationUnitDto.class))).willReturn(organisationUnit);
        BDDMockito.given(service.update(any(Long.class), any(OrganisationUnit.class))).willReturn(organisationUnit);
        BDDMockito.given(mapper.toDto(any(OrganisationUnit.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("OrganisationUnit 1"));

        verify(mapper, times(1)).fromDto(any(OrganisationUnitDto.class));
        verify(service, times(1)).update(any(Long.class), any(OrganisationUnit.class));
        verify(mapper, times(1)).toDto(any(OrganisationUnit.class));
    }

    @DisplayName("Should successfully delete organisation unit")
    @Test
    void shouldDeleteOrganisationUnit() throws Exception {
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
