package ch.puzzle.pcts.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.mapper.CertificateMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
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
@WebMvcTest(CertificateController.class)
class CertificateControllerIT {

    @MockitoBean
    private CertificateBusinessService service;

    @MockitoBean
    private CertificateMapper mapper;

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASEURL = "/api/v1/certificates";

    private Certificate certificate;
    private CertificateDto requestDto;
    private CertificateDto expectedDto;
    private Long id;

    @BeforeEach
    void setUp() {
        certificate = new Certificate(1L, "Certificate 1", new BigDecimal("5.5"), "This is Certificate 1");
        requestDto = new CertificateDto(null, "Certificate 1", new BigDecimal("5.5"), "This is Certificate 1");
        expectedDto = new CertificateDto(1L, "Certificate 1", new BigDecimal("5.5"), "This is Certificate 1");
        id = 1L;
    }

    @DisplayName("Should successfully get all certificates")
    @Test
    void shouldGetAllCertificates() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(certificate));
        BDDMockito.given(mapper.toDto(any(List.class))).willReturn(List.of(expectedDto));

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(expectedDto.id()))
                .andExpect(jsonPath("$[0].name").value(expectedDto.name()))
                .andExpect(jsonPath("$[0].points").value(expectedDto.points()))
                .andExpect(jsonPath("$[0].comment").value(expectedDto.comment()));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(any(List.class));
    }

    @DisplayName("Should successfully get certificate by id")
    @Test
    void shouldGetCertificateById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(certificate);
        BDDMockito.given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Certificate 1"))
                .andExpect(jsonPath("$.points").value(new BigDecimal("5.5")))
                .andExpect(jsonPath("$.comment").value("This is Certificate 1"));

        verify(service, times(1)).getById((1L));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully create new certificate")
    @Test
    void shouldCreateNewCertificate() throws Exception {
        BDDMockito.given(mapper.fromDto(any(CertificateDto.class))).willReturn(certificate);
        BDDMockito.given(service.create(any(Certificate.class))).willReturn(certificate);
        BDDMockito.given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Certificate 1"))
                .andExpect(jsonPath("$.points").value(new BigDecimal("5.5")))
                .andExpect(jsonPath("$.comment").value("This is Certificate 1"));

        verify(mapper, times(1)).fromDto(any(CertificateDto.class));
        verify(service, times(1)).create(any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully update certificate")
    @Test
    void shouldUpdateCertificate() throws Exception {
        BDDMockito.given(mapper.fromDto(any(CertificateDto.class))).willReturn(certificate);
        BDDMockito.given(service.update(any(Long.class), any(Certificate.class))).willReturn(certificate);
        BDDMockito.given(mapper.toDto(any(Certificate.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Certificate 1"))
                .andExpect(jsonPath("$.points").value(new BigDecimal("5.5")))
                .andExpect(jsonPath("$.comment").value("This is Certificate 1"));

        verify(mapper, times(1)).fromDto(any(CertificateDto.class));
        verify(service, times(1)).update(any(Long.class), any(Certificate.class));
        verify(mapper, times(1)).toDto(any(Certificate.class));
    }

    @DisplayName("Should successfully delete certificate")
    @Test
    void shouldDeleteCertificate() throws Exception {
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
