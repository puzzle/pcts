package ch.puzzle.pcts.controller;

import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
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
    private final ConfigurationMapper mapper;
    private final AuthorizationConfiguration authConfig;

    public ConfigurationController(ConfigurationMapper mapper, AuthorizationConfiguration authConfig) {
        this.mapper = mapper;
        this.authConfig = authConfig;
    }

    @GetMapping
    public ResponseEntity<ConfigurationDto> getAuthorizationConfiguration() {
        return ResponseEntity.ok(mapper.toDto(this.authConfig));
    }
}
