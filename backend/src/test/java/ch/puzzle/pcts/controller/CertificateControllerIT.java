package ch.puzzle.pcts.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.mapper.CertificateMapper;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.service.business.CertificateBusinessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
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
public class CertificateControllerIT {

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
    private CertificateDto dto;

    @BeforeEach
    void setUp() {
        certificate = new Certificate(1L, "Certificate 1", new BigDecimal("5.5"), "This is Certificate 1");
        requestDto = new CertificateDto(null, "Certificate 1", new BigDecimal("5.5"), "This is Certificate 1");
        dto = new CertificateDto(1L, "Certificate 1", new BigDecimal("5.5"), "This is Certificate 1");
    }

    @DisplayName("Should successfully get all Certificates")
    @Test
    void shouldGetAllCertificates() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(Collections.singletonList(certificate));
        BDDMockito.given(mapper.toDto(certificate)).willReturn(dto);

        mvc
                .perform(get(BASEURL)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(dto.id()))
                .andExpect(jsonPath("$[0].name").value(dto.name()))
                .andExpect(jsonPath("$[0].points").value(dto.points()))
                .andExpect(jsonPath("$[0].comment").value(dto.comment()));

        verify(service, times(1)).getAll();
        verify(mapper, times(1)).toDto(certificate);
    }
}
