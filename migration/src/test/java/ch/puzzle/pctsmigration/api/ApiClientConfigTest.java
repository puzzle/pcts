package ch.puzzle.pctsmigration.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.TokenSupplier;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.CertificateTypesApi;
import org.openapitools.client.api.CertificatesApi;
import org.openapitools.client.api.MembersApi;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ApiClientConfigTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Mock
    private TokenSupplier tokenSupplier;

    private ApiClientConfig config;

    @BeforeEach
    void setUp() {
        config = new ApiClientConfig();
        ReflectionTestUtils.setField(config, "basePath", BASE_URL);
    }

    @Test
    @DisplayName("The apiClient bean should configure the base URL and authorization interceptor correctly")
    void apiClient_createsAndConfiguresApiClientWithInterceptor() {
        String expectedToken = "secret-jwt-token-12345";
        when(tokenSupplier.get()).thenReturn(expectedToken);

        ApiClient apiClient = config.apiClient(tokenSupplier);

        assertThat(apiClient.getBaseUri()).isEqualTo(BASE_URL);
        assertThat(apiClient.getRequestInterceptor()).isNotNull();

        Consumer<HttpRequest.Builder> interceptor = apiClient.getRequestInterceptor();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(BASE_URL));

        interceptor.accept(requestBuilder);
        HttpRequest builtRequest = requestBuilder.build();

        assertThat(builtRequest.headers().firstValue("Authorization")).isPresent().hasValue("Bearer " + expectedToken);

        verify(tokenSupplier, times(1)).get();
    }

    @Test
    @DisplayName("The certificateTypesApi bean should be initialized successfully")
    void certificateTypesApi_createsApiWithGivenClient() {
        ApiClient mockClient = mock(ApiClient.class);

        CertificateTypesApi api = config.certificateTypesApi(mockClient);

        assertThat(api).isNotNull();
    }

    @Test
    @DisplayName("The membersApi bean should be initialized successfully")
    void membersApi_createsApiWithGivenClient() {
        ApiClient mockClient = mock(ApiClient.class);

        MembersApi api = config.membersApi(mockClient);

        assertThat(api).isNotNull();
    }

    @Test
    @DisplayName("The certificatesApi bean should be initialized successfully")
    void certificatesApi_createsApiWithGivenClient() {
        ApiClient mockClient = mock(ApiClient.class);

        CertificatesApi api = config.certificatesApi(mockClient);

        assertThat(api).isNotNull();
    }
}