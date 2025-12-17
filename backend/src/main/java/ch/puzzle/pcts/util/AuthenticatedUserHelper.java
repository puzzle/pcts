package ch.puzzle.pcts.util;

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

    @Value("${pcts.authentication.username-claim:name}")
    public void setUsernameClaim(String usernameClaim) {
        AuthenticatedUserHelper.usernameClaim = usernameClaim;
    }

    public static String getDisplayName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getCredentials() instanceof Jwt jwt) {
            final String username = jwt.getClaimAsString(usernameClaim);

            if (username == null) {
                String message = "Could not extract name from JWT from the claim '%s', returning subject instead"
                        .formatted(usernameClaim);
                log.warn(message);

                return jwt.getSubject();
            }

            return username;
        }

        log
                .warn("Could not extract name from security context because the authentication object was no JWT, returning authentication name instead");
        return authentication.getName();
    }
}
