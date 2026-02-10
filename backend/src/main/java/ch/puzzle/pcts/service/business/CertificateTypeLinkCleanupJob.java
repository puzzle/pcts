package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.persistence.CertificateTypePersistenceService;
import jakarta.transaction.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class CertificateTypeLinkCleanupJob {

    private final CertificateTypePersistenceService certificateTypePersistenceService;
    private final HttpClient httpClient;
    private static final Logger log = LoggerFactory.getLogger(CertificateTypeLinkCleanupJob.class);

    public CertificateTypeLinkCleanupJob(CertificateTypePersistenceService certificateTypePersistenceService) {
        this.certificateTypePersistenceService = certificateTypePersistenceService;
        this.httpClient = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Scheduled(cron = "${app.link-check.cron:0 0 3 * * ?}")
    @Transactional
    public void validateCertificateLinks() {
        List<CertificateType> certificatesWithLinks = certificateTypePersistenceService.findAllWhereLinkIsNotNull();

        for (CertificateType cert : certificatesWithLinks) {
            String url = cert.getLink();

            if (url == null || url.isBlank()) {
                continue;
            }

            checkSingleCertificate(cert, url);
        }
    }

    private void checkSingleCertificate(CertificateType cert, String url) {
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(5))
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            int status = response.statusCode();

            if (status >= 200 && status < 300) {
                cert.resetLinkStatus();
                log.debug("Link valid for ID {}: {}", cert.getId(), url);
            } else {
                cert.recordLinkFailure();
                log.warn("Link broken (Status {}) for ID {}: {}", status, cert.getId(), url);
            }

        } catch (Exception e) {
            cert.recordLinkFailure();
            log.error("Link unreachable for ID {}: {}", cert.getId(), e.getMessage());
        }

        certificateTypePersistenceService.save(cert);
    }
}
