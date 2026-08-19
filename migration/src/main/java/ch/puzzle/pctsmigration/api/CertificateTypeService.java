package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificateTypesApi;
import org.openapitools.client.model.CertificateTypeDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class CertificateTypeService {

    private final CertificateTypesApi certificateTypesApi;

    public CertificateTypeService(CertificateTypesApi certificateTypesApi) {
        this.certificateTypesApi = certificateTypesApi;
    }

    public List<CertificateTypeDto> getCertificateTypes() {
        try {
            return this.certificateTypesApi.getCertificateTypes();
        } catch (ApiException e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), e.getMessage()));
        }
    }
}
