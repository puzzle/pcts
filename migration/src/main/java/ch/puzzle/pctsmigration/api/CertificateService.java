package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificatesApi;
import org.openapitools.client.model.CertificateDto;
import org.openapitools.client.model.CertificateInputDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {
    private final CertificatesApi certificatesApi;
    private final Logger logger = LoggerFactory.getLogger(CertificateService.class);

    public CertificateService(CertificatesApi certificatesApi) {
        this.certificatesApi = certificatesApi;
    }

    public void create(List<CertificateInputDto> dtos) {
        List<Long> createdIds = new ArrayList<>();

        for (CertificateInputDto dto : dtos) {
            try {
                CertificateDto created = this.certificatesApi.createCertificate(dto);
                createdIds.add(created.getId());
            } catch (ApiException e) {
                rollbackCreatedCertificates(createdIds);
                throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                       "Migration aborted. Reason: " + e.getMessage()));
            }
        }
    }

    private void rollbackCreatedCertificates(List<Long> createdIds) {
        for (Long id : createdIds) {
            try {
                this.certificatesApi.deleteCertificate(id);
            } catch (ApiException rollbackException) {
                this.logger.error("Rollback failed for ID {}", id, rollbackException);
            }
        }
    }
}
