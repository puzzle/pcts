package ch.puzzle.pcts.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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
        given(securityService.isAdmin()).willReturn(true);

        return SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("org_hr"));
    }

    protected SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor ownerJwt() {
        given(securityService.isAdmin()).willReturn(false);
        given(securityService.isOwner(any())).willReturn(true);

        return SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("org_members"));
    }
}
