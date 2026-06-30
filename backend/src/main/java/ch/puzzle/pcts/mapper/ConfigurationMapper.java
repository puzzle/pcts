package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.configuration.KeycloakConfiguration;
import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationMapper {
    private final KeycloakConfiguration keycloakConfig;

    public ConfigurationMapper(KeycloakConfiguration keycloakConfig) {
        this.keycloakConfig = keycloakConfig;
    }

    public ConfigurationDto toDto(AuthorizationConfiguration authConfig) {
        return new ConfigurationDto(new ConfigurationDto.KeycloakDto(keycloakConfig.url(),
                                                                     keycloakConfig.realm(),
                                                                     keycloakConfig.clientId(),
                                                                     authConfig.adminAuthorities()));
    }
}
