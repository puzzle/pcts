package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.help")
public record HelpUrlConfiguration(@NotBlank String url) {
    public String helpUrl() {
        if (isValid()) {
            return url;
        } else {
            throw new IllegalStateException("Invalid support url configurated");
        }
    }

    private boolean isValid() {
        return !url.substring(8).isBlank() && url.startsWith("https://") && url.length() > 8;
    }
}
