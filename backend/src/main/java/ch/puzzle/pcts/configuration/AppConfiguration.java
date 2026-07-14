package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.AssertTrue;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pcts.app")
public record AppConfiguration(String helpUrl) {
    public String getHelpUrl() {
        return helpUrl;
    }

    @AssertTrue(message = "The help URL is not valid")
    public boolean isValid() {
        if (helpUrl == null) {
            return false;
        }

        if (helpUrl.isBlank()) {
            return false;
        }

        if (helpUrl.contains(" ")) {
            return false;
        }

        return Stream.of("https://", "mailto:", "tel:", "sms").anyMatch(helpUrl::startsWith);
    }
}
