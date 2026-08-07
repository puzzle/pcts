package ch.puzzle.pctsmigration;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.CertificateTypesApi;
import org.openapitools.client.api.MembersApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiClientConfig {

    @Value("${backend.api.base-url}")
    private String basePath;

    @Value("${backend.api.key}")
    private String apiKey;

    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(basePath);
        apiClient.setRequestInterceptor(builder -> builder.header("X-API-Key", apiKey));

        return apiClient;
    }

    @Bean
    public CertificateTypesApi certificateTypesApi(ApiClient apiClient) {
        return new CertificateTypesApi(apiClient);
    }

    @Bean
    public MembersApi membersApi(ApiClient apiClient) {
        return new MembersApi(apiClient);
    }
}
