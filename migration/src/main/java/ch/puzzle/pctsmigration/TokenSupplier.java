package ch.puzzle.pctsmigration;

import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class TokenSupplier {

    private final OAuth2AuthorizedClientManager manager;

    private final OAuth2ClientProperties oAuth2ClientProperties;

    public TokenSupplier(OAuth2AuthorizedClientManager manager, OAuth2ClientProperties oAuth2ClientProperties) {
        this.manager = manager;
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }


    public String get() {
        List<OAuth2ClientProperties.Registration> registrationList = oAuth2ClientProperties.getRegistration().values().stream().toList();
        String clientId = registrationList.getFirst().getClientId();

        if (clientId == null){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not resolve clientId from application.properties");
        }

        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                .withClientRegistrationId(clientId)
                .principal("migration-service")
                .build();

        OAuth2AuthorizedClient client = manager.authorize(request);
        if (client == null) {
            throw new IllegalStateException("Failed to obtain token for migration-client");
        }
        return client.getAccessToken().getTokenValue();
    }
}