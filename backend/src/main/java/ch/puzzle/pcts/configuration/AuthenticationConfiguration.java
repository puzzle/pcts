package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pcts.security.authentication")
public record AuthenticationConfiguration(@NotBlank @DefaultValue("name") String usernameClaim,
        @NotBlank @DefaultValue("email") String emailClaim) {
}
