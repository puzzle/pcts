package ch.puzzle.pcts.controller;

import static ch.puzzle.pcts.util.TestData.CERTIFICATE_1_ID;
import static ch.puzzle.pcts.util.TestDataDTOs.*;
import static ch.puzzle.pcts.util.TestDataModels.CERTIFICATE_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.mapper.CertificateMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
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
@WebMvcTest(CertificateController.class)
class CertificateControllerIT {

    @MockitoBean
    private CertificateBusinessService service;

    @MockitoBean
    private CertificateMapper mapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JsonMapper jsonMapper;

    private static final String BASEURL = "/api/v1/certificates";

    @DisplayName("Should successfully get certificate by id")
    @Test
    void shouldGetCertificateById() throws Exception {
        when(service.getById(CERTIFICATE_1_ID)).thenReturn(CERTIFICATE_1);
        when(mapper.toDto(any(Certificate.class))).thenReturn(CERTIFICATE_1_DTO);

        mvc
                .perform(get(BASEURL + "/{id}", CERTIFICATE_1_ID).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(CERTIFICATE_1_DTO, "$"));

        verify(service, times(1)).getById(CERTIFICATE_1_ID);
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully create new certificate")
    @Test
    void shouldCreateNewMemberCertificate() throws Exception {
        when(mapper.fromDto(any(CertificateInputDto.class))).thenReturn(CERTIFICATE_1);
        when(service.create(any(Certificate.class))).thenReturn(CERTIFICATE_1);
        when(mapper.toDto(any(Certificate.class))).thenReturn(CERTIFICATE_1_DTO);

        mvc
                .perform(post(BASEURL)
                        .content(jsonMapper.writeValueAsString(CERTIFICATE_2_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(CERTIFICATE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(CertificateInputDto.class));
        verify(service, times(1)).create(any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully update certificate")
    @Test
    void shouldUpdateCertificate() throws Exception {
        when(mapper.fromDto(any(CertificateInputDto.class))).thenReturn(CERTIFICATE_1);
        when(service.update(eq(CERTIFICATE_1_ID), any(Certificate.class))).thenReturn(CERTIFICATE_1);
        when(mapper.toDto(any(Certificate.class))).thenReturn(CERTIFICATE_1_DTO);

        mvc
                .perform(put(BASEURL + "/{id}", CERTIFICATE_1_ID)
                        .content(jsonMapper.writeValueAsString(CERTIFICATE_1_INPUT))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(CERTIFICATE_1_DTO, "$"));

        verify(mapper, times(1)).fromDto(any(CertificateInputDto.class));
        verify(service, times(1)).update(eq(CERTIFICATE_1_ID), any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully delete certificate")
    @Test
    void shouldDeleteCertificate() throws Exception {
        doNothing().when(service).delete(CERTIFICATE_1_ID);

        mvc
                .perform(delete(BASEURL + "/{id}", CERTIFICATE_1_ID).with(csrf()))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());

        verify(service, times(1)).delete(CERTIFICATE_1_ID);
    }
}