package ch.puzzle.pcts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.configuration.AuthenticationConfiguration;
import ch.puzzle.pcts.exception.PCTSException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private AuthenticationConfiguration authConfiguration;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return email when claim is present in JWT")
    public void shouldReturnEmailFromJwtClaim() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            setupSecurityContext(mockedContext);

            String emailClaim = "email";
            String emailValue = "user@puzzle.ch";

            when(authConfiguration.emailClaim()).thenReturn(emailClaim);
            when(authentication.getCredentials()).thenReturn(jwt);
            when(jwt.getClaimAsString(emailClaim)).thenReturn(emailValue);

            Optional<String> result = userService.getEmail();

            assertTrue(result.isPresent());
            assertEquals(emailValue, result.get());
        }
    }

    @Test
    @DisplayName("Should return empty when no authentication exists")
    public void shouldReturnEmptyWhenNoAuth() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(null);

            Optional<String> result = userService.getEmail();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    @DisplayName("Should return username claim if present")
    public void shouldReturnUsernameClaim() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            setupSecurityContext(mockedContext);

            when(authConfiguration.usernameClaim()).thenReturn("preferred_username");
            when(authentication.getCredentials()).thenReturn(jwt);
            when(jwt.getClaimAsString("preferred_username")).thenReturn("tester");

            assertEquals("tester", userService.getDisplayName());
        }
    }

    @Test
    @DisplayName("Should fall back to Subject if claim is missing")
    public void shouldFallbackToSubject() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            setupSecurityContext(mockedContext);

            when(authConfiguration.usernameClaim()).thenReturn("preferred_username");
            when(authentication.getCredentials()).thenReturn(jwt);
            when(jwt.getClaimAsString("preferred_username")).thenReturn(null);
            when(jwt.getSubject()).thenReturn("subject-123");

            assertEquals("subject-123", userService.getDisplayName());
        }
    }

    @Test
    @DisplayName("Should return 1L if email exists")
    public void shouldReturnIdIfEmailExists() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            setupSecurityContext(mockedContext);

            when(authConfiguration.emailClaim()).thenReturn("email");
            when(authentication.getCredentials()).thenReturn(jwt);
            when(jwt.getClaimAsString("email")).thenReturn("info@puzzle.ch");

            assertEquals(1L, userService.getIdFromEmail());
        }
    }

    @Test
    @DisplayName("Should throw PCTSException if email is missing")
    public void shouldThrowExceptionWhenEmailMissing() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            setupSecurityContext(mockedContext);

            when(authConfiguration.emailClaim()).thenReturn("email");
            when(authentication.getCredentials()).thenReturn(null); // Force standard auth path
            when(authentication.getName()).thenReturn(null);

            assertThrows(PCTSException.class, () -> userService.getIdFromEmail());
        }
    }

    private void setupSecurityContext(MockedStatic<SecurityContextHolder> mockedContext) {
        mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
}