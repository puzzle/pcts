// package ch.puzzle.pctsmigration;
//
// import ch.puzzle.pctsmigration.certificates.CertificateExtractionPipeline;
// import ch.puzzle.pctsmigration.extractor.ExtractorService;
// import java.util.List;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Nested;
// import org.junit.jupiter.api.Test;
// import org.openapitools.client.ApiException;
// import org.openapitools.client.model.CertificateInputDto;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.mock.web.MockMultipartFile;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.web.servlet.MockMvc;
//
// import static org.mockito.ArgumentMatchers.eq;
// import static org.mockito.Mockito.*;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// @WebMvcTest(MigrationController.class)
// class MigrationControllerTest {
//
// @Autowired
// private MockMvc mockMvc;
//
// // Hinweis: @MockitoBean ersetzt seit Spring Boot 3.4 das alte @MockBean aus
// Spring Boot Test
// @MockitoBean
// private ExtractorService extractorService;
//
// @MockitoBean
// private CertificateExtractionPipeline certificateExtractionPipeline;
//
// @Nested
// @DisplayName("POST /api/ai/certificates")
// class CertificatesEndpointTests {
//
// @Test
// @DisplayName("Sollte Datei extrahieren, Zertifikate anlegen und HTTP 201
// Created mit JSON-Body zurückgeben")
// void certificates_whenValidFileUploaded_extractsCreatesAndReturns201() throws
// Exception {
// // Given
// MockMultipartFile file = new MockMultipartFile(
// "file",
// "sbb_certificates.ods",
// "application/vnd.oasis.opendocument.spreadsheet",
// "dummy ods content".getBytes()
// );
//
// CertificateInputDto dto1 = new CertificateInputDto();
// dto1.setMemberId(1L);
// dto1.setComment("Erstes Zertifikat");
//
// CertificateInputDto dto2 = new CertificateInputDto();
// dto2.setMemberId(2L);
// dto2.setComment("Zweites Zertifikat");
//
// List<CertificateInputDto> extractedDtos = List.of(dto1, dto2);
//
// when(extractorService.extract(eq(file), eq(certificateExtractionPipeline)))
// .thenReturn(extractedDtos);
//
// // When / Then: Request abschicken und Response prüfen
// mockMvc.perform(multipart("/api/ai/certificates")
// .file(file)
// .contentType(MediaType.MULTIPART_FORM_DATA))
// .andExpect(status().isCreated()) // HTTP 201
// .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
// .andExpect(jsonPath("$.length()").value(2))
// .andExpect(jsonPath("$[0].memberId").value(1))
// .andExpect(jsonPath("$[0].comment").value("Erstes Zertifikat"))
// .andExpect(jsonPath("$[1].memberId").value(2))
// .andExpect(jsonPath("$[1].comment").value("Zweites Zertifikat"));
//
// // Sicherstellen, dass extract(...) UND create(...) im Hintergrund ausgeführt
// wurden
// verify(extractorService, times(1)).extract(eq(file),
// eq(certificateExtractionPipeline));
// verify(certificateExtractionPipeline, times(1)).create(eq(extractedDtos));
// }
//
// @Test
// @DisplayName("Sollte Fehler weiterwerfen und create nicht aufrufen, wenn die
// Extraktion fehlschlägt")
// void certificates_whenExtractionFails_throwsExceptionAndDoesNotCreate()
// throws Exception {
// // Given
// MockMultipartFile file = new MockMultipartFile(
// "file",
// "corrupted.ods",
// "application/vnd.oasis.opendocument.spreadsheet",
// "bad content".getBytes()
// );
//
// when(extractorService.extract(eq(file), eq(certificateExtractionPipeline)))
// .thenThrow(new ApiException("API Error during extraction"));
//
// // When / Then: Erwartet den Status-Code aus deinem GlobalExceptionHandler
// // (Standard für ApiException im GlobalExceptionHandler ist HTTP 502)
// mockMvc.perform(multipart("/api/ai/certificates")
// .file(file)
// .contentType(MediaType.MULTIPART_FORM_DATA))
// .andExpect(status().isBadGateway()); // HTTP 502 gemäß handleApiException
//
// verify(extractorService, times(1)).extract(eq(file),
// eq(certificateExtractionPipeline));
// verify(certificateExtractionPipeline, never()).create(any());
// }
//
// @Test
// @DisplayName("Sollte HTTP 400 Bad Request zurückgeben, wenn der Request kein
// 'file'-Part enthält")
// void certificates_whenFilePartMissing_returns400() throws Exception {
// // When / Then: Multipart-Request völlig ohne angehängte Datei senden
// mockMvc.perform(multipart("/api/ai/certificates")
// .contentType(MediaType.MULTIPART_FORM_DATA))
// .andExpect(status().isBadRequest());
//
// verifyNoInteractions(extractorService);
// verifyNoInteractions(certificateExtractionPipeline);
// }
// }
// }