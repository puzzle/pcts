package ch.puzzle.pcts.security;

import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("SecurityService")
public class SecurityService {
    @Value("#{'${pcts.authentication.admin-authorities}'.split(',')}")
    private List<String> adminAuthorities;

    public boolean isAdmin() {
        return getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(getAdminAuthoritiesAsRoles()::contains);
    }

    public boolean isUsersOwnSheet() {
        // TODO
        return false;
    }

    private List<String> getAdminAuthoritiesAsRoles() {
        return this.adminAuthorities.stream().map(a -> "SCOPE_" + a).toList();
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}
