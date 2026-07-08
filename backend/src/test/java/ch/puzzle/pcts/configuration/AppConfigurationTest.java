package ch.puzzle.pcts.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AppConfigurationTest {
    @DisplayName("Should return correct url a valid url is configured")
    @Test
    void shouldReturnCorrectUrlWhenUrlIsValid() {
        String url = "https://example.com";

        var config = new AppConfiguration(url);

        assertEquals(url, config.helpUrl());
    }
}
