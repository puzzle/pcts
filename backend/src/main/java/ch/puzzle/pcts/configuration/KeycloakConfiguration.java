package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pcts.frontend.keycloak")
public record KeycloakConfiguration(@NotBlank String url, @NotBlank String realm, @NotBlank String clientId) {
}
