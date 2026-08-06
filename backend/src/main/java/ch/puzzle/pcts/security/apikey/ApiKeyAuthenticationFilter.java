package ch.puzzle.pcts.security.apikey;

import ch.puzzle.pcts.configuration.AuthenticationConfiguration;
import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.model.apikey.ApiKey;
import ch.puzzle.pcts.service.business.ApiKeyBusinessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(ApiKeyAuthenticationFilter.class);
    private static final String API_KEY_HEADER = "X-API-Key";

    private final ApiKeyBusinessService apiKeyBusinessService;
    private final AuthorizationConfiguration authorizationConfiguration;
    private final AuthenticationConfiguration authenticationConfiguration;

    public ApiKeyAuthenticationFilter(ApiKeyBusinessService apiKeyBusinessService,
                                      AuthorizationConfiguration authorizationConfiguration,
                                      AuthenticationConfiguration authenticationConfiguration) {
        this.apiKeyBusinessService = apiKeyBusinessService;
        this.authorizationConfiguration = authorizationConfiguration;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String rawKey = request.getHeader(API_KEY_HEADER);
        if (rawKey == null || rawKey.isBlank()) {
            log.debug("No or blank value in header '{}', skipping authentication via API key", API_KEY_HEADER);
            filterChain.doFilter(request, response);
            return;
        }

        if (!authenticationConfiguration.enableApiKeys()) {
            log.warn("Authentication request with an API key but API keys are disabled");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Optional<ApiKey> validKey = apiKeyBusinessService.isValid(rawKey);
        if (validKey.isEmpty()) {
            log.warn("Authentication request with an invalid or revoked API key");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        ApiKey apiKey = validKey.get();
        log.debug("Successfully authenticated with API key '{}'", apiKey.getName());

        updateLastUsed(apiKey);
        updateSecurityContext(apiKey);

        filterChain.doFilter(request, response);
    }

    private void updateSecurityContext(ApiKey apiKey) {
        List<SimpleGrantedAuthority> authorities = authorizationConfiguration
                .adminAuthoritiesAsRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        SecurityContextHolder
                .getContext()
                .setAuthentication(new ApiKeyAuthenticationToken(apiKey.getId(), authorities));
    }

    private void updateLastUsed(ApiKey apiKey) {
        apiKey.setLastUsed(LocalDateTime.now());
        apiKeyBusinessService.update(apiKey.getId(), apiKey);
    }
}
