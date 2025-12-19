package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.ROLE_2_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.ROLE_2_DTO;
import static ch.puzzle.pcts.util.TestDataDTOs.ROLE_2_INPUT;
import static ch.puzzle.pcts.util.TestDataModels.ROLE_2;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.mapper.RoleMapper;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.business.RoleBusinessService;
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

@ControllerIT(RoleController.class)
class RoleControllerIT extends ControllerITBase {

    @MockitoBean
    private RoleBusinessService service;

    @MockitoBean
    private RoleMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/roles";

    @DisplayName("Should successfully get all roles")
    @Test
    void shouldGetAllRoles() throws Exception {
        when(service.getAll()).thenReturn(List.of(ROLE_2));
        when(mapper.toDto(any(List.class))).thenReturn(List.of(ROLE_2_DTO));

        mvc
                .perform(get(BASEURL).with(csrf()).with(adminJwt()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(ROLE_2_DTO, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get role by id")
    @Test
    void shouldGetRoleById() throws Exception {
        when(service.getById(anyLong())).thenReturn(ROLE_2);
        when(mapper.toDto(any(Role.class))).thenReturn(ROLE_2_DTO);

        mvc
                .perform(get(BASEURL + "/1").with(csrf()).with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(ROLE_2_DTO, "$"));

        verify(service, times(1)).getById((1L));
        verify(mapper, times(1)).toDto(any(Role.class));
    }

    @DisplayName("Should successfully create new role")
    @Test
    void shouldCreateNewRole() throws Exception {
        when(mapper.fromDto(any(RoleDto.class))).thenReturn(ROLE_2);
        when(service.create(any(Role.class))).thenReturn(ROLE_2);
        when(mapper.toDto(any(Role.class))).thenReturn(ROLE_2_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(ROLE_2_DTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(ROLE_2_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(RoleDto.class));
        verify(service, times(1)).create(any(Role.class));
        verify(mapper, times(1)).toDto(any(Role.class));
    }

    @DisplayName("Should successfully update role")
    @Test
    void shouldUpdateRole() throws Exception {
        when(mapper.fromDto(any(RoleDto.class))).thenReturn(ROLE_2);
        when(service.update(any(Long.class), any(Role.class))).thenReturn(ROLE_2);
        when(mapper.toDto(any(Role.class))).thenReturn(ROLE_2_DTO);

        mvc
                .perform(put(BASEURL + "/" + ROLE_2_ID)
                        .content(jsonMapper.writeValueAsString(ROLE_2_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(ROLE_2_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(RoleDto.class));
        verify(service, times(1)).update(any(Long.class), any(Role.class));
        verify(mapper, times(1)).toDto(any(Role.class));
    }

    @DisplayName("Should successfully delete role")
    @Test
    void shouldDeleteRole() throws Exception {
        doNothing().when(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + ROLE_2_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
