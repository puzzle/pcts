package ch.puzzle.pcts.configuration;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthenticationConfigurationTest {
    @DisplayName("Should correctly store and retrieve username and email claims")
    @Test
    void shouldStorePropertiesCorrectly() {
        String expectedUsername = "preferred_username";
        String expectedEmail = "user_email";

        var config = new AuthenticationConfiguration(expectedUsername, expectedEmail);

        assertEquals(expectedUsername, config.usernameClaim(), "Username claim should match input");
        assertEquals(expectedEmail, config.emailClaim(), "Email claim should match input");
    }
}