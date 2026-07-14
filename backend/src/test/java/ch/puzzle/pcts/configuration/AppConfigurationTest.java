package ch.puzzle.pcts.configuration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class AppConfigurationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("Should return violations when an invalid url is provided")
    @ParameterizedTest
    @ValueSource(strings = { "", "http://example.com" })
    @NullSource
    void shouldCreateViolations(String input) {
        var config = new AppConfiguration(input);
        var violations = validator.validate(config);

        assertFalse(violations.isEmpty(), "Expected violations for: " + input);
    }

    @DisplayName("Should not return any violations when a valid url is provided")
    @ParameterizedTest
    @ValueSource(strings = { "https://example.com", "https://example.ch", "mailto:test@example.ch", "tel:+12345678910",
            "sms:+12345678910" })
    void shouldNotCreateViolations(String input) {
        var config = new AppConfiguration(input);
        var violations = validator.validate(config);

        assertTrue(violations.isEmpty(), "Expected no violations for: " + input);
    }

}