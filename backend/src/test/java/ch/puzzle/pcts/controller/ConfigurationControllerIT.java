package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.configuration.AppConfiguration;
import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.dto.appconfiguration.AppConfigurationDto;
import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
import ch.puzzle.pcts.mapper.AppConfigurationMapper;
import ch.puzzle.pcts.mapper.AuthorizationMapper;
import ch.puzzle.pcts.util.JsonDtoMatcher;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerIT(ConfigurationController.class)
class ConfigurationControllerIT extends ControllerITBase {
    @MockitoBean
    private AuthorizationConfiguration authorizationConfiguration;

    @MockitoBean
    private AppConfiguration appConfiguration;

    @MockitoBean
    private AuthorizationMapper configMapper;

    @MockitoBean
    private AppConfigurationMapper appConfigurationMapper;

    @Autowired
    private MockMvc mvc;

    private static final String BASEURL = "/api/v1/configuration";

    private ConfigurationDto configurationDto;
    private AppConfigurationDto appConfigurationDto;

    @BeforeEach
    void setUp() {
        this.appConfigurationDto = new AppConfigurationDto("https://example.com");
        this.configurationDto = new ConfigurationDto(new ConfigurationDto.KeycloakDto("http://localhost:8544",
                                                                                      "pitc",
                                                                                      "pcts-dev",
                                                                                      List.of("ADMIN_1", "ADMIN_2")));
    }

    @DisplayName("Should successfully get configuration")
    @Test
    void shouldSuccessfullyGetConfiguration() throws Exception {
        when(configMapper.toDto(authorizationConfiguration)).thenReturn(configurationDto);

        mvc
                .perform(get(BASEURL + "/authorization").with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(configurationDto, "$"));
    }

    @DisplayName("Should successfully get support page url")
    @Test
    void shouldSuccessfullyGetSupportPageUrl() throws Exception {
        when(appConfigurationMapper.toDto(any())).thenReturn(appConfigurationDto);

        mvc
                .perform(get(BASEURL + "/app").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(appConfigurationDto, "$"));
    }

}