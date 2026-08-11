package ch.puzzle.pctsmigration.api;

import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificatesApi;
import org.openapitools.client.model.CertificateInputDto;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {
    private final CertificatesApi certificatesApi;

    public CertificateService(CertificatesApi certificatesApi) {
        this.certificatesApi = certificatesApi;
    }

    public void create(List<CertificateInputDto> dtos) throws ApiException {
        for (CertificateInputDto dto : dtos) {
            this.certificatesApi.createCertificate(dto);
        }
    }
}
