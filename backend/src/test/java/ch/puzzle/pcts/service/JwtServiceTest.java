package ch.puzzle.pcts.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.configuration.AuthenticationConfiguration;
import ch.puzzle.pcts.exception.PCTSException;
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
class JwtServiceTest {

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
    @DisplayName("Should return username claim if present")
    void shouldReturnUsernameClaim() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            setupSecurityContext(mockedContext);

            when(authConfiguration.displayNameClaim()).thenReturn("preferred_username");
            when(authentication.getCredentials()).thenReturn(jwt);
            when(jwt.getClaimAsString("preferred_username")).thenReturn("tester");

            assertEquals("tester", jwtService.getDisplayName());
        }
    }

    @Test
    @DisplayName("Should throw exception  if claim is missing")
    void shouldFallbackToSubject() {
        try (MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class)) {
            setupSecurityContext(mockedContext);

            when(authConfiguration.displayNameClaim()).thenReturn("preferred_username");
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