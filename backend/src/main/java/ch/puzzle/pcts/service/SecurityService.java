package ch.puzzle.pcts.service;

import ch.puzzle.pcts.configuration.AuthorisationConfiguration;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.Collection;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("SecurityService")
public class SecurityService {
    private final UserService userService;
    private final MemberBusinessService memberService;
    private final AuthorisationConfiguration authorisationConfiguration;

    public SecurityService(UserService userService, MemberBusinessService memberService,
                           AuthorisationConfiguration authorisationConfiguration) {
        this.userService = userService;
        this.memberService = memberService;
        this.authorisationConfiguration = authorisationConfiguration;
    }

    public boolean isAdmin() {
        return getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authorisationConfiguration.adminAuthoritiesAsRoles()::contains);
    }

    public boolean isOwner(Long userId) {
        Optional<Member> member = memberService.findIfExists(userId);
        if (member.isEmpty()) {
            return false;
        }

        Optional<String> email = this.userService.getEmail();
        if (email.isEmpty()) {
            return false;
        }

        if (member.get().getEmail() == null) {
            return false;
        }

        return member.get().getEmail().equals(email.get());
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}
