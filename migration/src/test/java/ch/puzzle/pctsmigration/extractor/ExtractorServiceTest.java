package ch.puzzle.pctsmigration.extractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.OdsParserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class ExtractorServiceTest {

    private static final String MARKDOWN = "## Sheet: Test\n| Data |";
    private static final DummyResult AI_RESULT = new DummyResult("Valid Data");
    private static final DummyDto EXPECTED_DTO = new DummyDto("Mapped DTO");

    @Mock
    private OdsParserService odsParserService;

    @Mock
    private AiService aiService;

    @Mock
    private Validator validator;

    @Mock
    private ExtractionPipeline<String, DummyResult, DummyDto> pipeline;

    @InjectMocks
    private ExtractorService extractorService;

    private MockMultipartFile sampleFile;

    @BeforeEach
    void setUp() {
        sampleFile = new MockMultipartFile("file",
                                           "test.ods",
                                           "application/vnd.oasis.opendocument.spreadsheet",
                                           "dummy content".getBytes());
    }

    @Test
    @DisplayName("The pipeline should run successfully")
    void extract_whenValidationDisabled_returnsMappedDtos() {
        mockPipelineExtraction(sampleFile);
        when(validator.validate(any())).thenReturn(Collections.emptySet());
        when(pipeline.mapToDto("test.ods", AI_RESULT)).thenReturn(List.of(EXPECTED_DTO));

        List<DummyDto> result = extractorService.extract(sampleFile, pipeline);

        assertThat(result).containsExactly(EXPECTED_DTO);
    }

    @Test
    @DisplayName("should throw migration exception when jakarta validation fails")
    void extract_whenJakartaValidationFails_throwsMigrationException() {
        mockPipelineExtraction(sampleFile);

        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        when(validator.validate(any())).thenReturn(Set.of(violation));

        MigrationException exception = assertThrows(MigrationException.class,
                                                    () -> extractorService.extract(sampleFile, pipeline));

        assertThat(exception.getError().status()).isEqualTo(HttpStatusCode.valueOf(400));
    }

    @Test
    @DisplayName("should throw migration exception when filename invalid is")
    void extract_whenFilenameInvalidIs_throwsMigrationException() {
        MultipartFile invalidFile = new MockMultipartFile("file",
                                                          " ",
                                                          "application/vnd.oasis.opendocument.spreadsheet",
                                                          "dummy content".getBytes());

        mockPipelineExtraction(invalidFile);
        when(validator.validate(any())).thenReturn(Collections.emptySet());

        MigrationException exception = assertThrows(MigrationException.class,
                                                    () -> extractorService.extract(invalidFile, pipeline));

        assertThat(exception.getError().status()).isEqualTo(HttpStatusCode.valueOf(400));
        assertThat(exception.getError().message()).isEqualTo("File name is empty");
    }

    private void mockPipelineExtraction(MultipartFile file) {
        when(odsParserService.parseToPromptText(file, new ArrayList<>())).thenReturn(MARKDOWN);
        when(pipeline.fetchContext()).thenReturn("Context");
        when(pipeline.systemPrompt("Context")).thenReturn("System Prompt");
        when(pipeline.entityClass()).thenReturn(DummyResult.class);
        when(aiService.extract(MARKDOWN, "System Prompt", DummyResult.class)).thenReturn(AI_RESULT);
    }

    record DummyResult(String data) {
    }

    record DummyDto(String value) {
    }
}