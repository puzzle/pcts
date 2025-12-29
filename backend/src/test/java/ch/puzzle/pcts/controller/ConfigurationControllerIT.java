package ch.puzzle.pcts.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
import ch.puzzle.pcts.mapper.ConfigurationMapper;
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
    private ConfigurationMapper mapper;

    @Autowired
    private MockMvc mvc;

    private static final String BASEURL = "/api/v1/configuration";

    private ConfigurationDto configurationDto;

    @BeforeEach
    void setUp() {
        this.configurationDto = new ConfigurationDto(List.of("ADMIN_1", "ADMIN_2"));
    }

    @DisplayName("Should successfully get configuration")
    @Test
    void shouldSuccessfullyGetConfiguration() throws Exception {
        given(mapper.toDto(authorizationConfiguration)).willReturn(configurationDto);

        mvc
                .perform(get(BASEURL).with(csrf()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(JsonDtoMatcher.matchesDto(configurationDto, "$"));
    }

}