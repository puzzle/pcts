package ch.puzzle.pcts.security.apikey;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class ApiKeyAuthenticationTokenTest {

    @DisplayName("Credentials should set properties correctly")
    @Test
    void shouldReturnAlwaysNullForCredentials() {
        var authorities = List.of(new SimpleGrantedAuthority("admin"));

        ApiKeyAuthenticationToken result = new ApiKeyAuthenticationToken(12L, authorities);

        assertEquals(12L, result.getPrincipal());
        assertEquals(authorities, result.getAuthorities());
    }

    @DisplayName("equals() should compare apiKeyId")
    @ParameterizedTest
    @CsvSource({ "1,1,true", "1, 2, false" })
    void shouldCompareOnlyApiKeyIdOnlyWhenEqualsIsCalled(long id1, long id2, boolean expected) {
        ApiKeyAuthenticationToken key1 = new ApiKeyAuthenticationToken(id1, List.of());
        ApiKeyAuthenticationToken key2 = new ApiKeyAuthenticationToken(id2, List.of());

        boolean result = key1.equals(key2);

        assertEquals(expected, result);
    }

}