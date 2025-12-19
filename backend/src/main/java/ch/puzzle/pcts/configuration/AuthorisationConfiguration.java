package ch.puzzle.pcts.configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "pcts.security.authorisation")
public record AuthorisationConfiguration(@NotBlank @DefaultValue("[pitc][roles]") String authoritiesSpElExpression,
        @NotEmpty @DefaultValue() List<@NotBlank String> adminAuthorities) {
    public List<String> adminAuthoritiesAsRoles() {
        return this.adminAuthorities.stream().map(a -> "SCOPE_" + a).toList();
    }
}
