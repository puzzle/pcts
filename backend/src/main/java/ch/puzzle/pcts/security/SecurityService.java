package ch.puzzle.pcts.security;

import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import ch.puzzle.pcts.util.AuthenticatedUserHelper;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("SecurityService")
public class SecurityService {
    private final MemberBusinessService memberService;

    @Value("#{'${pcts.authentication.admin-authorities}'.split(',')}")
    private List<String> adminAuthorities;

    public SecurityService(MemberBusinessService memberService) {
        this.memberService = memberService;
    }

    public boolean isAdmin() {
        return getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(getAdminAuthoritiesAsRoles()::contains);
    }

    public boolean isUsersOwnSheet(Long userId) {
        Optional<Member> member = memberService.findIfExists(userId);
        if (member.isEmpty()) {
            return false;
        }

        Optional<String> email = AuthenticatedUserHelper.getEmail();
        if (email.isEmpty()) {
            return false;
        }

        return false;
    }

    private List<String> getAdminAuthoritiesAsRoles() {
        return this.adminAuthorities.stream().map(a -> "SCOPE_" + a).toList();
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}
