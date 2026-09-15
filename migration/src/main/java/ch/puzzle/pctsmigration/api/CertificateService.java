package ch.puzzle.pctsmigration.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificatesApi;
import org.openapitools.client.model.CertificateDto;
import org.openapitools.client.model.CertificateInputDto;
import org.springframework.stereotype.Service;

@Service
public class CertificateService extends CreationBase<CertificateInputDto, CertificateDto> {
    private final CertificatesApi certificatesApi;

    public CertificateService(CertificatesApi certificatesApi) {
        this.certificatesApi = certificatesApi;
    }

    @Override
    protected CertificateDto executeCreate(CertificateInputDto dto) throws ApiException {
        return this.certificatesApi.createCertificate(dto);
    }

    @Override
    protected void executeDelete(Long id) throws ApiException {
        this.certificatesApi.deleteCertificate(id);
    }

    @Override
    protected Long extractId(CertificateDto entity) {
        return entity.getId();
    }
}
