package ch.puzzle.pctsmigration.service;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificateTypesApi;
import org.openapitools.client.api.DegreeTypesApi;
import org.openapitools.client.model.DegreeTypeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateApiService {
    private final CertificateTypesApi certificateTypesApi;
    private final DegreeTypesApi degreeTypesApi;

    public CertificateApiService(CertificateTypesApi certificateTypesApi, DegreeTypesApi degreeTypesApi) {
        this.certificateTypesApi = certificateTypesApi;
        this.degreeTypesApi = degreeTypesApi;
    }

    public List<DegreeTypeDto> getCertificates() throws ApiException {
        return this.degreeTypesApi.getDegreeType();
    }
}
