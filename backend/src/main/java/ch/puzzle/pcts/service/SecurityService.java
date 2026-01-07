package ch.puzzle.pcts.service;

import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for evaluating security constraints used in Method
 * Security expressions.
 * <p>
 * This service is referenced directly in SpEL (Spring Expression Language)
 * annotations such as:
 * <ul>
 * <li>{@link ch.puzzle.pcts.security.annotation.IsAdmin}</li>
 * <li>{@link ch.puzzle.pcts.security.annotation.IsOwner}</li>
 * <li>{@link ch.puzzle.pcts.security.annotation.IsAdminOrOwner}</li>
 * </ul>
 * <p>
 */
@Service("SecurityService")
public class SecurityService {
    private final JwtService jwtService;
    private final MemberBusinessService memberService;
    private final AuthorizationConfiguration authorizationConfiguration;

    public SecurityService(JwtService jwtService, MemberBusinessService memberService,
                           AuthorizationConfiguration authorizationConfiguration) {
        this.jwtService = jwtService;
        this.memberService = memberService;
        this.authorizationConfiguration = authorizationConfiguration;
    }

    public boolean isAdmin() {
        return getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authorizationConfiguration.adminAuthoritiesAsRoles()::contains);
    }

    public boolean isOwner(Long userId) {
        Optional<Member> member = memberService.findIfExists(userId);
        if (member.isEmpty()) {
            return false;
        }

        Optional<String> email = this.jwtService.getEmail();
        if (email.isEmpty()) {
            return false;
        }

        if (member.get().getEmail() == null) {
            return false;
        }

        return member.get().getEmail().equals(email.get());
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        Authentication auth = Optional
                .ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .orElse(null);
        return auth != null ? auth.getAuthorities() : Collections.emptyList();
    }

}
