package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.help")
public record HelpUrlConfiguration(@NotBlank @DefaultValue("https://google.com") String url) {
    public String helpUrl() {
        return url;
    }
}
