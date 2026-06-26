package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationMapper {
    public ConfigurationDto toDto(AuthorizationConfiguration authConfig) {
        return new ConfigurationDto(authConfig.adminAuthorities());
    }
}
