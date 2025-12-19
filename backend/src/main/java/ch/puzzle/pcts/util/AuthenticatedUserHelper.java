package ch.puzzle.pcts.util;

import java.util.Optional;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserHelper {
    private static final Logger log = LoggerFactory.getLogger(AuthenticatedUserHelper.class);
    private static String usernameClaim;
    private static String emailClaim;

    @Value("${pcts.authentication.username-claim:name}")
    public void setUsernameClaim(String usernameClaim) {
        AuthenticatedUserHelper.usernameClaim = usernameClaim;
    }

    @Value("${pcts.authentication.email-claim:email}")
    public static void setEmailClaim(String emailClaim) {
        AuthenticatedUserHelper.emailClaim = emailClaim;
    }

    public static String getDisplayName() {
        Function<Jwt, Optional<String>> useSubjectInstead = (jwt) -> Optional.ofNullable(jwt.getSubject());
        Optional<String> name = getProperty(usernameClaim, useSubjectInstead);

        return name.orElseThrow();
    }

    public static Optional<String> getEmail() {
        return AuthenticatedUserHelper.getProperty(emailClaim, (jwt) -> Optional.empty());
    }

    private static Optional<String> getProperty(String propertyName, Function<Jwt, Optional<String>> backupFunction) {
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
