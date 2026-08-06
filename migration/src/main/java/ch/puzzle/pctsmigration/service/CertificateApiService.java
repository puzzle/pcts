package ch.puzzle.pctsmigration.service;

import ch.puzzle.pctsmigration.config.ApiClientFactory;
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificateTypesApi;
import org.openapitools.client.model.CertificateTypeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateApiService {
    private final ApiClientFactory apiClientFactory;

    public CertificateApiService(ApiClientFactory apiClientFactory) {
        this.apiClientFactory = apiClientFactory;
    }

    public List<CertificateTypeDto> getCertificates(String token, String markdown) throws ApiException {
        ApiClient apiClient = this.apiClientFactory.createCertificateApi(token);
        CertificateTypesApi api = new CertificateTypesApi(apiClient);
        return api.getCertificateTypes();
    }
}
