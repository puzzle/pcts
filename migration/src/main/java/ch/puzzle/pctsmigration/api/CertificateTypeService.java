package ch.puzzle.pctsmigration.api;

import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificateTypesApi;
import org.openapitools.client.model.CertificateTypeDto;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeService {

    private final CertificateTypesApi certificateTypesApi;

    public CertificateTypeService(CertificateTypesApi certificateTypesApi) {
        this.certificateTypesApi = certificateTypesApi;
    }

    public List<CertificateTypeDto> getCertificateTypes() throws ApiException {
        return this.certificateTypesApi.getCertificateTypes();
    }
}
