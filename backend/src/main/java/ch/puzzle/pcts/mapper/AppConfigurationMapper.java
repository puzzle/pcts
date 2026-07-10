package ch.puzzle.pcts.mapper;

import ch.puzzle.pcts.dto.appconfiguration.AppConfigurationDto;
import org.springframework.stereotype.Component;

@Component
public class AppConfigurationMapper {
    public AppConfigurationDto toDto(String url) {
        return new AppConfigurationDto(url);
    }
}
