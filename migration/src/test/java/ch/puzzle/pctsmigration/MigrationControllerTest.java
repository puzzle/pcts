package ch.puzzle.pctsmigration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pctsmigration.certificates.CertificateExtractionPipeline;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.extractor.ExtractorService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.model.CertificateInputDto;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class MigrationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ExtractorService extractorService;

    @Mock
    private CertificateExtractionPipeline pipeline;

    @InjectMocks
    private MigrationController migrationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(migrationController).build();
    }

    @Test
    void testCertificate_Success() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file",
                                                           "data.ods",
                                                           "application/vnd.oasis.opendocument.spreadsheet",
                                                           "dummy content".getBytes());

        CertificateInputDto dummyDto = new CertificateInputDto();
        when(extractorService.extract(any(), eq(pipeline))).thenReturn(List.of(dummyDto));

        mockMvc
                .perform(multipart("/api/migration/certificate").file(mockFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(extractorService, times(1)).extract(any(), eq(pipeline));
        verify(pipeline, times(1)).create(anyList());
    }

    @Test
    void testCertificates_AllFilesSuccessful() throws Exception {
        MockMultipartFile file1 = new MockMultipartFile("files", "file1.ods", "text/plain", "content1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "file2.ods", "text/plain", "content2".getBytes());

        CertificateInputDto dummyDto1 = new CertificateInputDto();
        CertificateInputDto dummyDto2 = new CertificateInputDto();

        when(extractorService.extract(any(), eq(pipeline)))
                .thenReturn(List.of(dummyDto1))
                .thenReturn(List.of(dummyDto2));

        mockMvc
                .perform(multipart("/api/migration/certificates").file(file1).file(file2))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.successfulCertificates").isMap())
                .andExpect(jsonPath("$.successfulCertificates.length()").value(2))
                .andExpect(jsonPath("$.failedFiles").isArray())
                .andExpect(jsonPath("$.failedFiles.length()").value(0));

        verify(extractorService, times(2)).extract(any(), eq(pipeline));
        verify(pipeline, times(2)).create(anyList());
    }

    @Test
    void testCertificates_PartialFailureAndSuccess() throws Exception {
        MockMultipartFile successFile = new MockMultipartFile("files", "success.ods", "text/plain", "good".getBytes());
        MockMultipartFile errorFile = new MockMultipartFile("files", "error.ods", "text/plain", "bad".getBytes());

        CertificateInputDto dummyDto = new CertificateInputDto();

        MigrationException mockException = mock(MigrationException.class);
        when(mockException.getMessage()).thenReturn("Parsing failed miserably");

        when(extractorService.extract(any(), eq(pipeline))).thenReturn(List.of(dummyDto)).thenThrow(mockException);

        mockMvc
                .perform(multipart("/api/migration/certificates").file(successFile).file(errorFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.successfulCertificates.length()").value(1))
                .andExpect(jsonPath("$.failedFiles.length()").value(1))
                .andExpect(jsonPath("$.failedFiles[0].filename").value("error.ods"));
        verify(pipeline, times(1)).create(anyList());
    }

    @Test
    void testCertificates_MissingFileReturns400() throws Exception {
        mockMvc.perform(multipart("/api/migration/certificate")).andExpect(status().isBadRequest());
    }
}