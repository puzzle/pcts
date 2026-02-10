package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.CERT_TYPE_5_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.CERT_TYPE_5_DTO;
import static ch.puzzle.pcts.util.TestDataDTOs.CERT_TYPE_5_Input;
import static ch.puzzle.pcts.util.TestDataModels.CERT_TYPE_5;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.mapper.CertificateTypeMapper;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

@ControllerIT(CertificateTypeController.class)
class CertificateTypeControllerIT extends ControllerITBase {

    @MockitoBean
    private CertificateTypeBusinessService service;

    @MockitoBean
    private CertificateTypeMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/certificate-types";

    @DisplayName("Should successfully get all certificate types")
    @Test
    void shouldGetAllCertificateTypes() throws Exception {
        when(service.getAll()).thenReturn(List.of(CERT_TYPE_5));
        when(mapper.toDto(any(List.class))).thenReturn(List.of(CERT_TYPE_5_DTO));

        mvc
                .perform(get(BASEURL).with(csrf()).with(adminJwt()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(JsonDtoMatcher.matchesDto(CERT_TYPE_5_DTO, "$[0]"));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get certificate type by id")
    @Test
    void shouldGetCertificateTypeById() throws Exception {
        when(service.getById(anyLong())).thenReturn(CERT_TYPE_5);
        when(mapper.toDto(any(CertificateType.class))).thenReturn(CERT_TYPE_5_DTO);

        mvc
                .perform(get(BASEURL + "/1").with(csrf()).with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(CERT_TYPE_5_DTO, "$"));

        verify(service, times(1)).getById((1L));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully create new certificate type")
    @Test
    void shouldCreateNewCertificateType() throws Exception {
        when(mapper.fromDto(any(CertificateTypeDto.class))).thenReturn(CERT_TYPE_5);
        when(service.create(any(CertificateType.class))).thenReturn(CERT_TYPE_5);
        when(mapper.toDto(any(CertificateType.class))).thenReturn(CERT_TYPE_5_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(CERT_TYPE_5_Input))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(CERT_TYPE_5_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(CertificateTypeDto.class));
        verify(service, times(1)).create(any(CertificateType.class));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully update certificate type")
    @Test
    void shouldUpdateCertificateType() throws Exception {
        when(mapper.fromDto(any(CertificateTypeDto.class))).thenReturn(CERT_TYPE_5);
        when(service.update(any(Long.class), any(CertificateType.class))).thenReturn(CERT_TYPE_5);
        when(mapper.toDto(any(CertificateType.class))).thenReturn(CERT_TYPE_5_DTO);

        mvc
                .perform(put(BASEURL + "/" + CERT_TYPE_5_ID)
                        .content(jsonMapper.writeValueAsString(CERT_TYPE_5_Input))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(CERT_TYPE_5_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(CertificateTypeDto.class));
        verify(service, times(1)).update(any(Long.class), any(CertificateType.class));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully delete certificate type")
    @Test
    void shouldDeleteCertificateType() throws Exception {
        doNothing().when(service).delete(anyLong());

        mvc
                .perform(delete(BASEURL + "/" + CERT_TYPE_5_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(adminJwt()))
                .andExpect(status().is(204))
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(any(Long.class));
    }
}
