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

    @DisplayName("Should be valid when URL is a correct https link ending in com")
    @Test
    void shouldBeValidWhenUrlIsCorrectAndEndingInCom() {
        var config = new AppConfiguration("https://example.com");
        var violations = validator.validate(config);

        assertTrue(violations.isEmpty(), "Expected no violations for valid URL");
    }

    @DisplayName("Should be valid when URL is a correct https link ending in ch")
    @Test
    void shouldBeValidWhenUrlIsCorrectAndEndingInCh() {
        var config = new AppConfiguration("https://example.ch");
        var violations = validator.validate(config);

        assertTrue(violations.isEmpty(), "Expected no violations for valid URL");
    }

    @DisplayName("Should have violations when URL is null")
    @Test
    void shouldHaveViolationsWhenUrlIsNull() {
        var config = new AppConfiguration(null);

        var violations = validator.validate(config);

        assertFalse(violations.isEmpty(), "Expected violations for invalid URL");
    }

    @DisplayName("Should have violations when URL is blank")
    @Test
    void shouldHaveViolationsWhenUrlIsBlank() {
        var config = new AppConfiguration("");
        var violations = validator.validate(config);
        assertFalse(violations.isEmpty(), "Expected violations for blank URL");
    }

    @DisplayName("Should have violations when URL does not start with https://")
    @Test
    void shouldHaveViolationsWhenUrlDoesNotStartWithHttps() {
        var config = new AppConfiguration("http://example.com");
        var violations = validator.validate(config);
        assertFalse(violations.isEmpty(), "Expected violations for URL not starting with https://");
    }

    @DisplayName("Should have violations when URL is shorter than 12 chars")
    @Test
    void shouldHaveViolationsWhenUrlIsShorterThan12Chars() {
        var config = new AppConfiguration("https://.ch");
        var violations = validator.validate(config);
        assertFalse(violations.isEmpty(), "Expected violations for URL shorter than 12 chars");
    }

    @DisplayName("Should have violations when URL does not contain .ch or .com")
    @Test
    void shouldHaveViolationsWhenUrlDoesNotContainChOrCom() {
        var config = new AppConfiguration("https://example.de");
        var violations = validator.validate(config);
        assertFalse(violations.isEmpty(), "Expected violations for URL not containing ch or com");
    }
}