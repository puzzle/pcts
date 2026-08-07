package ch.puzzle.pctsmigration;

import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

@Component
public class TokenSupplier {

    private final OAuth2AuthorizedClientManager manager;

    public TokenSupplier(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }

    public String get() {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                .withClientRegistrationId("migration-client")
                .principal("migration-service")
                .build();
        OAuth2AuthorizedClient client = manager.authorize(request);
        if (client == null) {
            throw new IllegalStateException("Failed to obtain token for migration-client");
        }
        return client.getAccessToken().getTokenValue();
    }
}