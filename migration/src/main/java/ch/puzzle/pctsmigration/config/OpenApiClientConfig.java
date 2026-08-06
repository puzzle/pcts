package ch.puzzle.pctsmigration.config;

import org.openapitools.client.ApiClient;
import org.openapitools.client.api.CertificateTypesApi;
import org.openapitools.client.api.DegreeTypesApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiClientConfig {

    @Value("${backend.api.base-url}")
    private String basePath;


    @Value("${backend.api.token}")
    private String apiToken;

    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(basePath);
        apiClient.setRequestInterceptor(builder -> builder.header("Authorization", "Bearer " + apiToken));

        return apiClient;
    }

    @Bean
    public CertificateTypesApi certificateTypesApi(ApiClient apiClient) {
        return new CertificateTypesApi(apiClient);
    }

    @Bean
    public DegreeTypesApi degreeTypesApi(ApiClient apiClient) {
        return new DegreeTypesApi(apiClient);
    }
}