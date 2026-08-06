package ch.puzzle.pcts.security.apikey;

import static ch.puzzle.pcts.util.TestDataModels.API_KEY_1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.configuration.AuthenticationConfiguration;
import ch.puzzle.pcts.configuration.AuthorizationConfiguration;
import ch.puzzle.pcts.service.business.ApiKeyBusinessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApiKeyAuthenticationFilterTest {
    @Mock
    ApiKeyBusinessService businessServiceMock;

    @Mock
    AuthorizationConfiguration authorizationConfigurationMock;

    @Mock
    AuthenticationConfiguration authenticationConfigurationMock;

    @InjectMocks
    ApiKeyAuthenticationFilter filter;

    HttpServletRequest request;
    HttpServletResponse response;
    FilterChain chain;

    @BeforeEach
    void setup() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @DisplayName("Should skip filter if header is empty")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void shouldSkipFilterIfHeaderIsEmpty(String headerValue) throws ServletException, IOException {
        when(request.getHeader(any())).thenReturn(headerValue);

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @DisplayName("Should deny if API keys are disabled")
    @Test
    void shouldDenyIfApiKeysAreDisabled() throws ServletException, IOException {
        when(request.getHeader(any())).thenReturn("key");
        when(authenticationConfigurationMock.enableApiKeys()).thenReturn(false);

        filter.doFilterInternal(request, response, chain);

        verify(chain, never()).doFilter(any(), any());
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @DisplayName("Should deny if no valid API key can be found")
    @Test
    void shouldDenyIfNoValidKey() throws ServletException, IOException {
        when(request.getHeader(any())).thenReturn("key");
        when(authenticationConfigurationMock.enableApiKeys()).thenReturn(true);
        when(businessServiceMock.isValid(any())).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, chain);

        verify(chain, never()).doFilter(any(), any());
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @DisplayName("Should update lastUsed be found")
    @Test
    void shouldUpdateTimestamp() throws ServletException, IOException {
        when(request.getHeader(any())).thenReturn("key");
        when(authenticationConfigurationMock.enableApiKeys()).thenReturn(true);
        when(businessServiceMock.isValid(any())).thenReturn(Optional.of(API_KEY_1));

        filter.doFilterInternal(request, response, chain);

        verify(chain, times(1)).doFilter(any(), any());
        verify(businessServiceMock).update(eq(API_KEY_1.getId()), any());
        verify(chain, times(1)).doFilter(any(), any());
    }
}