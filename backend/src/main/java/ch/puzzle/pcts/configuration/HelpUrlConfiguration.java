package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pcts.app.help")
public record HelpUrlConfiguration(@NotBlank String url) {
    public String helpUrl() {
        if (isValid()) {
            return url;
        } else {
            throw new IllegalStateException("Invalid support url configurated");
        }
    }

    private boolean isValid() {
        return url.startsWith("https://") && url.length() > 8 && !url.contains(" ") && !url.substring(8).isBlank();
    }
}
