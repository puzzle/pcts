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
        if (authentication.getCredentials() instanceof Jwt jwt) {
            final Optional<String> username = Optional.ofNullable(jwt.getClaimAsString(propertyName));

            if (username.isEmpty()) {
                final String message = String
                        .format("Could not extract property '%s' from security context, applying backup function",
                                propertyName);
                log.warn(message);
                return backupFunction.apply(jwt);
            }

            return username;
        }

        final String message = String
                .format("Could not extract property '%s' from security context because the authentication object was no JWT.",
                        propertyName);
        log.warn(message);

        return Optional.empty();
    }

}
