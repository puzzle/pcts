package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.configuration.AuthorisationConfiguration;
import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationMapper {
    public ConfigurationDto toDto(AuthorisationConfiguration authConfig) {
        return new ConfigurationDto(authConfig.adminAuthorities());
    }
}
