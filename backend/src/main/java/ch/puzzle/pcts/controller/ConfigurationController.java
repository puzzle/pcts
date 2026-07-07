package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.configuration.HelpUrlConfiguration;
import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
import ch.puzzle.pcts.dto.support.SupportDto;
import ch.puzzle.pcts.mapper.ConfigurationMapper;
import ch.puzzle.pcts.mapper.SupportMapper;
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
    private final SupportMapper supportMapper;
    private final AuthorizationConfiguration authConfig;
    private final HelpUrlConfiguration helpUrlConfig;

    public ConfigurationController(ConfigurationMapper configMapper, SupportMapper supportMapper,
                                   AuthorizationConfiguration authConfig, HelpUrlConfiguration helpUrlConfig) {
        this.configMapper = configMapper;
        this.supportMapper = supportMapper;
        this.authConfig = authConfig;
        this.helpUrlConfig = helpUrlConfig;
    }

    @GetMapping("/authorization")
    public ResponseEntity<ConfigurationDto> getAuthorizationConfiguration() {
        return ResponseEntity.ok(configMapper.toDto(this.authConfig));
    }

    @GetMapping("/help")
    public ResponseEntity<SupportDto> getHelpUrl() {
        return ResponseEntity.ok(supportMapper.toDto(this.helpUrlConfig.helpUrl()));
    }
}
