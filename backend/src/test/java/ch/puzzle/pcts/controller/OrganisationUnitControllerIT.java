package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.ORG_UNIT_1_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.ORG_UNIT_1_DTO;
import static ch.puzzle.pcts.util.TestDataDTOs.ORG_UNIT_1_INPUT;
import static ch.puzzle.pcts.util.TestDataModels.ORG_UNIT_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.mapper.OrganisationUnitMapper;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.service.business.OrganisationUnitBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@ControllerIT(OrganisationUnitController.class)
class OrganisationUnitControllerIT extends ControllerITBase {

    @MockitoBean
    private OrganisationUnitBusinessService service;

    @MockitoBean
    private OrganisationUnitMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/organisation-units";

    @DisplayName("Should successfully get all roles organisation units")
    @Test
    void shouldGetAllOrganisationUnits() throws Exception {
        when(service.getAll()).thenReturn(List.of(ORG_UNIT_1));
        when(mapper.toDto(any(List.class))).thenReturn(List.of(ORG_UNIT_1_DTO));

        mvc
                .perform(get(BASEURL).with(csrf()).with(adminJwt()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(ORG_UNIT_1_DTO, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get organisation unit by id")
    @Test
    void shouldGetOrganisationUnitById() throws Exception {
        when(service.getById(anyLong())).thenReturn(ORG_UNIT_1);
        when(mapper.toDto(any(OrganisationUnit.class))).thenReturn(ORG_UNIT_1_DTO);

        mvc
                .perform(get(BASEURL + "/1")
                        .with(csrf())

                        .with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(ORG_UNIT_1_DTO, "$"));

        verify(service, times(1)).getById(1L);
        verify(mapper, times(1)).toDto(any(OrganisationUnit.class));
    }

    @DisplayName("Should successfully create new organisation unit")
    @Test
    void shouldCreateNewOrganisationUnit() throws Exception {
        when(mapper.fromDto(any(OrganisationUnitDto.class))).thenReturn(ORG_UNIT_1);
        when(service.create(any(OrganisationUnit.class))).thenReturn(ORG_UNIT_1);
        when(mapper.toDto(any(OrganisationUnit.class))).thenReturn(ORG_UNIT_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(ORG_UNIT_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())

                        .with(adminJwt()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(ORG_UNIT_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(OrganisationUnitDto.class));
        verify(service, times(1)).create(any(OrganisationUnit.class));
        verify(mapper, times(1)).toDto(any(OrganisationUnit.class));
    }

    @DisplayName("Should successfully update organisation unit")
    @Test
    void shouldUpdateOrganisationUnit() throws Exception {
        when(mapper.fromDto(any(OrganisationUnitDto.class))).thenReturn(ORG_UNIT_1);
        when(service.update(any(Long.class), any(OrganisationUnit.class))).thenReturn(ORG_UNIT_1);
        when(mapper.toDto(any(OrganisationUnit.class))).thenReturn(ORG_UNIT_1_DTO);

        mvc
                .perform(put(BASEURL + "/" + ORG_UNIT_1_ID)
                        .content(jsonMapper.writeValueAsString(ORG_UNIT_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(ORG_UNIT_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(OrganisationUnitDto.class));
        verify(service, times(1)).update(any(Long.class), any(OrganisationUnit.class));
        verify(mapper, times(1)).toDto(any(OrganisationUnit.class));
    }

    @DisplayName("Should successfully delete organisation unit")
    @Test
    void shouldDeleteOrganisationUnit() throws Exception {
        doNothing().when(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + ORG_UNIT_1_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
