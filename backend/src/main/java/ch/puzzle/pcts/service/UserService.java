package ch.puzzle.pcts.service;

import ch.puzzle.pcts.configuration.AuthenticationConfiguration;
import ch.puzzle.pcts.exception.PCTSException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final AuthenticationConfiguration authConfiguration;

    public UserService(AuthenticationConfiguration authConfiguration) {
        this.authConfiguration = authConfiguration;
    }

    public String getDisplayName() {
        Function<Jwt, Optional<String>> useSubjectInstead = (jwt) -> Optional.ofNullable(jwt.getSubject());
        Optional<String> name = this.getProperty(authConfiguration.usernameClaim(), useSubjectInstead);

        return name.orElseThrow(() -> new PCTSException(HttpStatus.INTERNAL_SERVER_ERROR, List.of()));
    }

    public Optional<String> getEmail() {
        return this.getProperty(authConfiguration.emailClaim(), (jwt) -> Optional.empty());
    }

    private Optional<String> getProperty(String propertyName, Function<Jwt, Optional<String>> backupFunction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("No authentication found in SecurityContext during extraction of property '{}'", propertyName);
            return Optional.empty();
        }

        if (authentication.getCredentials() instanceof Jwt jwt) {
            final Optional<String> username = Optional.ofNullable(jwt.getClaimAsString(propertyName));

            if (username.isEmpty()) {
                log
                        .warn("Could not extract property '{}' from security context, applying backup function",
                              propertyName);
                return backupFunction.apply(jwt);
            }

            return username;
        }

        log
                .warn("Could not extract property '{}' from security context because the authentication object was not a JWT, returning standard authentication name instead.",
                      propertyName);
        return Optional.ofNullable(authentication.getName());
    }

}
