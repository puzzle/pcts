package ch.puzzle.pcts.security.apikey;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {
    private final Long apiKeyId;

    public ApiKeyAuthenticationToken(Long apiKeyId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKeyId = apiKeyId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKeyId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ApiKeyAuthenticationToken that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        return Objects.equals(apiKeyId, that.apiKeyId);
    }
}
