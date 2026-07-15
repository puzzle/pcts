package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.AssertTrue;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pcts.app")
public record AppConfiguration(String helpUrl) {

    @AssertTrue(message = "The help URL is not valid")
    public boolean isValid() {
        if (helpUrl == null || helpUrl.isBlank()) {
            return false;
        }

        // A url is not valid if it contains spaces
        if (helpUrl.contains(" ")) {
            return false;
        }

        return hasValidUrlPrefix(helpUrl);
    }

    private boolean hasValidUrlPrefix(String helpUrl) {
        return Stream.of("https://", "mailto:", "tel:", "sms:").anyMatch(helpUrl::startsWith);
    }
}
