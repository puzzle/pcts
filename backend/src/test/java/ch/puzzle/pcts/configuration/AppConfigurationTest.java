package ch.puzzle.pcts.configuration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class AppConfigurationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Should be valid when URL is a correct https link ending in com")
    @ParameterizedTest
    @CsvSource("""
            https://example.com, true,
            https://example.ch, true,
            null, false,
            , false,
            http://example.com, false,
            https://.ch, false,
            https://example.de, false,
            https://e x am.ch, false
            """)
    void shouldReturnExpected(String input, boolean expectedValid) {
        var config = new AppConfiguration(input);
        var violations = validator.validate(config);

        if (expectedValid) {
            assertTrue(violations.isEmpty(), "Expected no violations for: " + input);
        } else {
            assertFalse(violations.isEmpty(), "Expected violations for: " + input);
        }
    }
}