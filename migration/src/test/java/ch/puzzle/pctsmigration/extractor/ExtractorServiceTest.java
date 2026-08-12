package ch.puzzle.pctsmigration.extractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.ods.OdsParseResult;
import ch.puzzle.pctsmigration.ods.OdsParserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
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
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ExtractorServiceTest {

    @Mock
    private OdsParserService odsParserService;

    @Mock
    private ExtractorAiService aiService;

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
    void extract_whenValidationDisabled_returnsMappedDtos() throws Exception {
        String markdown = "## Sheet: Test\n| Data |";
        DummyResult aiResult = new DummyResult("Valid Data");
        DummyDto expectedDto = new DummyDto("Mapped DTO");

        when(odsParserService.parse(sampleFile)).thenReturn(new OdsParseResult(List.of()));
        when(odsParserService.toPromptText(any())).thenReturn(markdown);
        when(pipeline.fetchContext()).thenReturn("Context");
        when(pipeline.systemPrompt("Context")).thenReturn("System Prompt");
        when(pipeline.entityClass()).thenReturn(DummyResult.class);
        when(aiService.aiExtractFrom(eq(markdown), eq("System Prompt"), eq(DummyResult.class))).thenReturn(aiResult);
        when(pipeline.mapToDto(eq("test.ods"), eq(aiResult))).thenReturn(List.of(expectedDto));

        List<DummyDto> result = extractorService.extract(sampleFile, pipeline);

        assertThat(result).containsExactly(expectedDto);
        verifyNoInteractions(validator);
    }

    @Test
    @DisplayName("If the pipeline runs successfully when validation is enabled and no errors are found")
    void extract_whenValidationEnabledAndValid_returnsMappedDtos() throws Exception {
        DummyResult aiResult = new DummyResult("Valid Data");
        DummyDto expectedDto = new DummyDto("Mapped DTO");

        when(odsParserService.parse(sampleFile)).thenReturn(new OdsParseResult(List.of()));
        when(odsParserService.toPromptText(any())).thenReturn("markdown");
        when(pipeline.entityClass()).thenReturn(DummyResult.class);
        when(aiService.aiExtractFrom(anyString(), any(), eq(DummyResult.class))).thenReturn(aiResult);

        when(validator.validate(aiResult)).thenReturn(Collections.emptySet());
        when(pipeline.mapToDto(eq("test.ods"), eq(aiResult))).thenReturn(List.of(expectedDto));

        List<DummyDto> result = extractorService.extract(sampleFile, pipeline);

        assertThat(result).containsExactly(expectedDto);
        verify(validator).validate(aiResult);
    }

    @Test
    @DisplayName("Should throw an IllegalArgumentException if the LLM generates invalid data")
    void extract_whenValidationFails_throwsIllegalArgumentException() throws Exception {
        DummyResult aiResult = new DummyResult("Invalid Data");
        ConstraintViolation<DummyResult> violation = mock(ConstraintViolation.class);

        when(odsParserService.parse(sampleFile)).thenReturn(new OdsParseResult(List.of()));
        when(odsParserService.toPromptText(any())).thenReturn("markdown");
        when(pipeline.entityClass()).thenReturn(DummyResult.class);
        when(aiService.aiExtractFrom(anyString(), any(), eq(DummyResult.class))).thenReturn(aiResult);

        when(validator.validate(aiResult)).thenReturn(Set.of(violation));

        assertThatThrownBy(() -> extractorService.extract(sampleFile, pipeline))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("LLM generated invalid data");

        verify(pipeline, never()).mapToDto(anyString(), any());
    }

    @Test
    @DisplayName("Should pass an empty filename as a fallback if OriginalFilename is null")
    void extract_whenOriginalFilenameIsNull_passesEmptyStringToMapper() throws Exception {
        MockMultipartFile fileWithoutName = new MockMultipartFile("file", null, null, new byte[0]);
        DummyResult aiResult = new DummyResult("Data");

        when(odsParserService.parse(fileWithoutName)).thenReturn(new OdsParseResult(List.of()));
        when(odsParserService.toPromptText(any())).thenReturn("markdown");
        when(pipeline.entityClass()).thenReturn(DummyResult.class);
        when(aiService.aiExtractFrom(anyString(), any(), eq(DummyResult.class))).thenReturn(aiResult);
        when(pipeline.mapToDto(eq(""), eq(aiResult))).thenReturn(List.of());

        extractorService.extract(fileWithoutName, pipeline);

        verify(pipeline).mapToDto(eq(""), eq(aiResult));
    }

    record DummyResult(String data) {
    }

    record DummyDto(String value) {
    }
}