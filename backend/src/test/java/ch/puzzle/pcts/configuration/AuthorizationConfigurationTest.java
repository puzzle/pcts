package ch.puzzle.pcts.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthorizationConfigurationTest {

    @DisplayName("Should prefix roles with SCOPE_")
    @Test
    void shouldPrefixAdminAuthoritiesWithScope() {
        var config = new AuthorizationConfiguration("exp", List.of("ADMIN", "SUPERUSER"));

        List<String> roles = config.adminAuthoritiesAsRoles();

        assertThat(roles).containsExactly("SCOPE_ADMIN", "SCOPE_SUPERUSER");
    }

    @DisplayName("Should enforce immutability on adminAuthorities property")
    @Test
    void adminAuthoritiesShouldBeImmutableCopy() {
        var config = new AuthorizationConfiguration("exp", List.of("ADMIN"));
        List<String> authorities = config.adminAuthorities();

        assertThat(authorities).isEqualTo(List.of("ADMIN"));
        assertThrows(UnsupportedOperationException.class, () -> authorities.add("NEW"));
    }
}