package ch.puzzle.pcts.configuration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AppConfigurationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should be valid when URL is a correct https link")
    void shouldBeValidWhenUrlIsCorrect() {
        var config = new AppConfiguration("https://example.com");
        var violations = validator.validate(config);

        assertTrue(violations.isEmpty(), "Expected no violations for valid URL");
    }

    @Test
    @DisplayName("Should have violations when URL is blank or invalid")
    void shouldHaveViolationsWhenUrlIsInvalid() {
        var config = new AppConfiguration("invalid-url");

        var violations = validator.validate(config);

        assertFalse(violations.isEmpty(), "Expected violations for invalid URL");
    }
}