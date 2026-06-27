package ch.puzzle.pcts.mapper;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.dto.configuration.ConfigurationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigurationMapperTest {

    private ConfigurationMapper configurationMapper;

    @BeforeEach
    void setUp() {
        configurationMapper = new ConfigurationMapper(KEYCLOAK_CONFIGURATION);
    }

    @DisplayName("Should return configuration dto with keycloak and authorization details")
    @Test
    void shouldReturnConfigurationDto() {
        ConfigurationDto result = configurationMapper.toDto(AUTHORIZATION_CONFIGURATION);

        assertNotNull(result);
        assertNotNull(result.keycloak());
        assertEquals(KEYCLOAK_URL, result.keycloak().url());
        assertEquals(KEYCLOAK_REALM, result.keycloak().realm());
        assertEquals(KEYCLOAK_CLIENT_ID, result.keycloak().clientId());
        assertEquals(ADMIN_AUTHORITIES, result.keycloak().adminAuthorities());
    }
}
