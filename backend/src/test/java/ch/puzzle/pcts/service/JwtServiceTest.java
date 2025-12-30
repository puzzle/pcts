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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private AuthenticationConfiguration authConfiguration;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private JwtService jwtService;

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

            Optional<String> result = jwtService.getEmail();

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

            Optional<String> result = jwtService.getEmail();

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

            assertEquals("tester", jwtService.getDisplayName());
        }
    }

    @Test
    @DisplayName("Should throw exception  if claim is missing")
    public void shouldFallbackToSubject() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            setupSecurityContext(mockedContext);

            when(authConfiguration.usernameClaim()).thenReturn("preferred_username");
            when(authentication.getCredentials()).thenReturn(jwt);
            when(jwt.getClaimAsString("preferred_username")).thenReturn(null);

            PCTSException exception = assertThrows(PCTSException.class, () -> jwtService.getDisplayName());

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        }
    }

    private void setupSecurityContext(MockedStatic<SecurityContextHolder> mockedContext) {
        mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }
}