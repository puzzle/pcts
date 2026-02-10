package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.security.SpringSecurityConfig;
import ch.puzzle.pcts.service.SecurityService;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Import(SpringSecurityConfig.class)
public class ControllerITBase {
    @MockitoBean
    protected JwtDecoder jwtDecoder;

    @MockitoBean("SecurityService")
    protected SecurityService securityService;

    protected SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor adminJwt() {
        when(securityService.isAdmin()).thenReturn(true);

        return SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("org_hr"));
    }

    protected SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor ownerJwt() {
        when(securityService.isAdmin()).thenReturn(false);
        when(securityService.isOwner(any())).thenReturn(true);

        return SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("org_members"));
    }
}
