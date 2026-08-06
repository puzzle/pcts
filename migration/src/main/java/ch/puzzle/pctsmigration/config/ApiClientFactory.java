package ch.puzzle.pctsmigration.config;

import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiClientFactory {

    @Value("${backend.api.base-url}")
    private String basePath;

    public ApiClient createCertificateApi(String token) {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(basePath);
        apiClient.setRequestInterceptor(builder -> builder.header("Authorization", "Bearer " + token));
        return apiClient;
    }
}
