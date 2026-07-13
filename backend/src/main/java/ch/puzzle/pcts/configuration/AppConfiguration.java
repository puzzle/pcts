package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.AssertTrue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pcts.app")
public record AppConfiguration(String helpurl) {
    public String getHelpUrl() {
        return helpurl;
    }

    @AssertTrue(message = "Help url is not a valid url")
    public boolean isValid() {
        return helpurl != null && !helpurl.isBlank() && !helpurl.contains(" ") && helpurl.startsWith("https://")
               && helpurl.length() >= 12 && (helpurl.contains(".ch") || helpurl.contains(".com"));
    }
}
