package ch.puzzle.pcts.service;

import ch.puzzle.pcts.configuration.AuthenticationConfiguration;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final AuthenticationConfiguration authConfiguration;

    public JwtService(AuthenticationConfiguration authConfiguration) {
        this.authConfiguration = authConfiguration;
    }

    public String getDisplayName() {
        Optional<String> name = this.getProperty(authConfiguration.usernameClaim());

        return name.orElseThrow(() -> new PCTSException(HttpStatus.INTERNAL_SERVER_ERROR, List.of()));
    }

    public String getPreferredUsername() {
        Optional<String> prefName = this.getProperty(authConfiguration.preferredUsernameClaim());

        return prefName.orElseThrow(() -> new PCTSException(HttpStatus.INTERNAL_SERVER_ERROR, List.of()));
    }

    public Optional<String> getEmail() {
        return this.getProperty(authConfiguration.emailClaim());
    }

    public Member createMemberFromJwt() {
        String firstName = this.getProperty("given_name").orElseThrow(); // TODO: find a good exception for this
        String lastName = this.getProperty("family_name").orElseThrow(); // TODO: find a good exception for this
        String email = this.getProperty("email").orElseThrow(); // TODO: find a good exception for this
        String preferredUsername = this.getProperty("preferred_username").orElseThrow(); // TODO: find a good exception
                                                                                         // for this

        return Member.Builder
                .builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withPreferredUsername(preferredUsername)
                .withEmploymentState(EmploymentState.MEMBER) // if the member can login, he obviously is still working
                                                             // here
                .withBirthDate(LocalDate.of(1970, 1, 1)) // TODO: find a better solution to this
                .build();
    }

    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return !(auth instanceof AnonymousAuthenticationToken);
    }

    private Optional<String> getProperty(String propertyName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("No authentication found in SecurityContext during extraction of property '{}'", propertyName);
            return Optional.empty();
        }

        if (authentication.getCredentials() instanceof Jwt jwt) {
            final Optional<String> property = Optional.ofNullable(jwt.getClaimAsString(propertyName));

            if (property.isEmpty()) {
                log
                        .warn("Could not extract property '{}' from security context, applying backup function",
                              propertyName);
                return Optional.empty();
            }

            return property;
        }

        log
                .error("Could not extract property '{}' from security context because the authentication object was not a JWT.",
                       propertyName);
        return Optional.empty();
    }

}
