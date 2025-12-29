package ch.puzzle.pcts.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.service.business.MemberBusinessService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {
    @Mock
    UserService userService;

    @Mock
    MemberBusinessService memberBusinessService;

    @Mock
    AuthorizationConfiguration authorizationConfiguration;

    @InjectMocks
    SecurityService service;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @ParameterizedTest(name = "Roles {0} should NOT grant admin access against config {1}")
    @MethodSource("nonAdminProvider")
    void shouldReturnFalseOnIsAdminWhenAdminAuthoritiesDoesNotContainRole(List<String> userRoles,
                                                                          List<String> adminRoles) {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            mockSecurityContext(userRoles);
            when(authorizationConfiguration.adminAuthoritiesAsRoles()).thenReturn(adminRoles);

            assertFalse(service.isAdmin());
        }
    }

    @ParameterizedTest(name = "Role {0} should grant admin access via config {1}")
    @MethodSource("adminProvider")
    void shouldReturnTrueOnIsAdminWhenAdminAuthoritiesContainsRole(List<String> userRoles, List<String> adminRoles) {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            mockSecurityContext(userRoles);
            when(authorizationConfiguration.adminAuthoritiesAsRoles()).thenReturn(adminRoles);

            assertTrue(service.isAdmin());
        }
    }

    @Test
    @DisplayName("Should return false if member cannot be found")
    void shouldReturnFalseIfMemberCannotBeFound() {
        when(memberBusinessService.findIfExists(1L)).thenReturn(Optional.empty());
        assertFalse(service.isOwner(1L));
    }

    @Test
    @DisplayName("Should return false if current user email is empty")
    void shouldReturnFalseIfUserEmailIsEmpty() {
        Member member = new Member();
        member.setEmail("test@puzzle.ch");

        when(memberBusinessService.findIfExists(1L)).thenReturn(Optional.of(member));
        when(userService.getEmail()).thenReturn(Optional.empty());

        assertFalse(service.isOwner(1L));
    }

    @Test
    @DisplayName("Should return true if member email matches user email")
    void shouldReturnTrueIfEmailsMatch() {
        String email = "dev@puzzle.ch";
        Member member = new Member();
        member.setEmail(email);

        when(memberBusinessService.findIfExists(1L)).thenReturn(Optional.of(member));
        when(userService.getEmail()).thenReturn(Optional.of(email));

        assertTrue(service.isOwner(1L));
    }

    static Stream<Arguments> adminProvider() {
        return Stream
                .of(Arguments.of(List.of("ROLE_ADMIN"), List.of("ROLE_ADMIN", "ROLE_SUPERUSER")),
                    Arguments.of(List.of("ROLE_SUPERUSER"), List.of("ROLE_ADMIN", "ROLE_SUPERUSER")));
    }

    static Stream<Arguments> nonAdminProvider() {
        return Stream
                .of(Arguments.of(List.of("ROLE_USER"), List.of("ROLE_ADMIN")),
                    Arguments.of(Collections.emptyList(), List.of("ROLE_ADMIN")));
    }

    private void mockSecurityContext(List<String> userRoles) {
        var authorities = userRoles.stream().map(SimpleGrantedAuthority::new).toList();

        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().doReturn(authorities).when(authentication).getAuthorities();
        SecurityContextHolder.setContext(securityContext);
    }
}