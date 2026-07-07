package ch.puzzle.pcts.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HelpUrlConfigurationTest {
    @DisplayName("Should throw exception when url does not start with https://")
    @Test
    void shouldThrowExceptionWhenUrlDoesNotStartWithHttps() {
        String url = "http://example.com";

        var config = new HelpUrlConfiguration(url);

        assertThrows(IllegalStateException.class, () -> config.helpUrl());
    }

    @DisplayName("Should throw exception when url ends after https:// so when length is not over 8")
    @Test
    void shouldThrowExceptionWhenUrlIsNotLongerThan8Chars() {
        String url = "https://";

        var config = new HelpUrlConfiguration(url);

        assertThrows(IllegalStateException.class, () -> config.helpUrl());
    }

    @DisplayName("Should throw exception when url contains a space")
    @Test
    void shouldThrowExceptionWhenUrlContainsSpace() {
        String url = "https://in validUrl.com";

        var config = new HelpUrlConfiguration(url);

        assertThrows(IllegalStateException.class, () -> config.helpUrl());
    }

    @DisplayName("Should throw exception when url is blank after index 8")
    @Test
    void shouldThrowExceptionWhenUrlIsBlankAfterIndex8() {
        String url = "https://   ";

        var config = new HelpUrlConfiguration(url);

        assertThrows(IllegalStateException.class, () -> config.helpUrl());
    }

    @DisplayName("Should return correct url a valid url is configured")
    @Test
    void shouldReturnCorrectUrlWhenUrlIsValid() {
        String url = "https://example.com";

        var config = new HelpUrlConfiguration(url);

        assertEquals(url, config.helpUrl());
    }
}
