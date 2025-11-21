package ch.puzzle.pcts.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.SpringSecurityConfig;
import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.mapper.CertificateTypeMapper;
import ch.puzzle.pcts.mapper.ErrorMapper;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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
@WebMvcTest(CertificateTypeController.class)
class CertificateTypeControllerIT {

    @MockitoBean
    private CertificateTypeBusinessService service;

    @MockitoBean
    private CertificateTypeMapper mapper;

    @MockitoBean
    private ErrorMapper errorMapper;

    @Autowired
    private MockMvc mvc;

    ObjectMapper objectMapper = new ObjectMapper();

    private static final String BASEURL = "/api/v1/certificate-types";

    private CertificateType certificateType;
    private CertificateTypeDto requestDto;
    private CertificateTypeDto expectedDto;
    private Long id;

    @BeforeEach
    void setUp() {
        Set<Tag> tags = Set.of(new Tag(1L, "Tag 1"), new Tag(2L, "Tag 2"));
        certificateType = new CertificateType(1L,
                                              "Certificate Type 1",
                                              new BigDecimal("5.5"),
                                              "This is Certificate Type 1",
                                              tags);
        requestDto = new CertificateTypeDto(null,
                                            "Certificate Type 1",
                                            new BigDecimal("5.5"),
                                            "This is Certificate Type 1",
                                            tags.stream().map(Tag::getName).toList());
        expectedDto = new CertificateTypeDto(1L,
                                             "Certificate Type 1",
                                             new BigDecimal("5.5"),
                                             "This is Certificate Type 1",
                                             tags.stream().map(Tag::getName).toList());
        id = 1L;
    }

    @DisplayName("Should successfully get all certificate types")
    @Test
    void shouldGetAllCertificateTypes() throws Exception {
        BDDMockito.given(service.getAll()).willReturn(List.of(certificateType));
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

    @DisplayName("Should successfully get certificate type by id")
    @Test
    void shouldGetCertificateTypeById() throws Exception {
        BDDMockito.given(service.getById(anyLong())).willReturn(certificateType);
        BDDMockito.given(mapper.toDto(any(CertificateType.class))).willReturn(expectedDto);

        mvc
                .perform(get(BASEURL + "/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(service, times(1)).getById((1L));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully create new certificate type")
    @Test
    void shouldCreateNewCertificateType() throws Exception {
        BDDMockito.given(mapper.fromDto(any(CertificateTypeDto.class))).willReturn(certificateType);
        BDDMockito.given(service.create(any(CertificateType.class))).willReturn(certificateType);
        BDDMockito.given(mapper.toDto(any(CertificateType.class))).willReturn(expectedDto);

        mvc
                .perform(post(BASEURL)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(CertificateTypeDto.class));
        verify(service, times(1)).create(any(CertificateType.class));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully update certificate type")
    @Test
    void shouldUpdateCertificateType() throws Exception {
        BDDMockito.given(mapper.fromDto(any(CertificateTypeDto.class))).willReturn(certificateType);
        BDDMockito.given(service.update(any(Long.class), any(CertificateType.class))).willReturn(certificateType);
        BDDMockito.given(mapper.toDto(any(CertificateType.class))).willReturn(expectedDto);

        mvc
                .perform(put(BASEURL + "/" + id)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(expectedDto, "$"));

        verify(mapper, times(1)).fromDto(any(CertificateTypeDto.class));
        verify(service, times(1)).update(any(Long.class), any(CertificateType.class));
        verify(mapper, times(1)).toDto(any(CertificateType.class));
    }

    @DisplayName("Should successfully delete certificate type")
    @Test
    void shouldDeleteCertificateType() throws Exception {
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
