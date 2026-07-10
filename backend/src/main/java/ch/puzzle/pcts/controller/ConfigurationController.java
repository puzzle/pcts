package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.configuration.AppConfiguration;
import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.dto.appconfiguration.AppConfigurationDto;
import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
import ch.puzzle.pcts.mapper.AppConfigurationMapper;
import ch.puzzle.pcts.mapper.ConfigurationMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/configuration")
@Tag(name = "configuration", description = "Retrieve configuration properties for the application")
public class ConfigurationController {
    private final ConfigurationMapper configMapper;
    private final AuthorizationConfiguration authConfig;
    private final AppConfigurationMapper appConfigMapper;
    private final AppConfiguration appConfig;

    public ConfigurationController(ConfigurationMapper configMapper, AuthorizationConfiguration authConfig,
                                   AppConfigurationMapper appConfigMapper, AppConfiguration appConfig) {
        this.configMapper = configMapper;
        this.authConfig = authConfig;
        this.appConfigMapper = appConfigMapper;
        this.appConfig = appConfig;
    }

    @GetMapping("/authorization")
    public ResponseEntity<ConfigurationDto> getAuthorizationConfiguration() {
        return ResponseEntity.ok(configMapper.toDto(this.authConfig));
    }

    @GetMapping("/app")
    public ResponseEntity<AppConfigurationDto> getHelpUrl() {
        return ResponseEntity.ok(appConfigMapper.toDto(this.appConfig.getHelpUrl()));
    }
}
