package ch.puzzle.pctsmigration.extractor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;

class ExtractionPipelineTest {

    private DummyPipeline pipeline;

    @BeforeEach
    void setUp() {
        pipeline = new DummyPipeline();
    }

    @Test
    @DisplayName("extractAbbreviation should extract and uppercase the prefix before the underscore")
    void extractAbbreviation_withValidFilenameUnderscore_returnsUppercasePrefix() {
        String result = pipeline.extractAbbreviation("aw_zertifikate.ods");
        assertThat(result).isEqualTo("AW");

        String resultMultipleUnderscores = pipeline.extractAbbreviation("xy_some_other_file.ods");
        assertThat(resultMultipleUnderscores).isEqualTo("XY");
    }

    @Test
    @DisplayName("extractAbbreviation should extract and uppercase the prefix before the dot")
    void extractAbbreviation_withValidFilenameDot_returnsUppercasePrefix() {
        String result = pipeline.extractAbbreviation("aw.ods");
        assertThat(result).isEqualTo("AW");
    }

    @Test
    @DisplayName("extractAbbreviation should throw MigrationException if no underscore or dot is present")
    void extractAbbreviation_withInvalidFilename_throwsException() {
        String filename = "invalidfilename";

        MigrationException exception = assertThrows(MigrationException.class,
                                                    () -> pipeline.extractAbbreviation(filename));

        assertThat(exception.getError().status()).isEqualTo(HttpStatusCode.valueOf(400));
        assertThat(exception.getError().message())
                .isEqualTo("Invalid filename: can not extract abbreviation " + filename);
    }

    @Test
    @DisplayName("calculateDistance should return the correct Levenshtein distance")
    void calculateDistance_returnsCorrectLevenshteinDistance() {
        assertThat(pipeline.calculateDistance("Scrum Master", "Scrum Master")).isEqualTo(0);

        assertThat(pipeline.calculateDistance("kitten", "sitting")).isEqualTo(3);

        assertThat(pipeline.calculateDistance("Scrum Master", "Scrum Mstr")).isEqualTo(2);
    }

    @Test
    @DisplayName("additionalValidations should not throw any exceptions by default")
    void additionalValidations_doesNotThrowException() {
        assertThatCode(() -> pipeline.additionalValidations("dummy_object")).doesNotThrowAnyException();
    }

    private static class DummyPipeline extends ExtractionPipeline<Object, Object, Object> {

        @Override
        public Object fetchContext() {
            return null;
        }

        @Override
        public String systemPrompt(Object context) {
            return "";
        }

        @Override
        public OdsParseConfig odsSheetParseConfig() {
            return null;
        }

        @Override
        public Class<Object> entityClass() {
            return Object.class;
        }

        @Override
        public List<Object> mapToDto(String filename, Object wrapper) {
            return List.of();
        }

        @Override
        public void create(List<Object> dtos) {
        }
    }
}