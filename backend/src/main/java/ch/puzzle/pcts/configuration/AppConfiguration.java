package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pcts.app")
public record AppConfiguration(@NotBlank @URL String helpurl) {
    public String helpUrl() {
        return helpurl;
    }
}
