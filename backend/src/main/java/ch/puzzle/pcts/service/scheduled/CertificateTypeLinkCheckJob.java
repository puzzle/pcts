package ch.puzzle.pcts.service.scheduled;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CertificateTypeLinkCheckJob {

    private final CertificateTypeBusinessService certificateTypeBusinessService;
    private final HttpClient httpClient;
    private static final Logger log = LoggerFactory.getLogger(CertificateTypeLinkCheckJob.class);

    public CertificateTypeLinkCheckJob(CertificateTypeBusinessService certificateTypeBusinessService) {
        this.certificateTypeBusinessService = certificateTypeBusinessService;
        this.httpClient = HttpClient
                .newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Scheduled(cron = "${app.link-check.cron:0 0 3 * * ?}")
    @Transactional
    public void validateCertificateLinks() {
        List<CertificateType> certificatesWithLinks = certificateTypeBusinessService.findAllWhereLinkIsNotNull();

        int successCount = 0;
        int failureCount = 0;

        log.info("The link check has started. Checking {} links.", certificatesWithLinks.size());

        for (CertificateType cert : certificatesWithLinks) {
            String url = cert.getLink();

            if (url == null || url.isBlank()) {
                continue;
            }

            boolean isSuccess = checkSingleCertificate(cert, url);

            if (isSuccess) {
                successCount++;
            } else {
                failureCount++;
            }
        }

        log
                .info("The link check has finished. Total: {}, Success: {}, Failed: {}.",
                      successCount + failureCount,
                      successCount,
                      failureCount);
    }

    private boolean checkSingleCertificate(CertificateType cert, String url) {
        boolean success = false;
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(url))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(5))
                    .build();

            int status = httpClient.send(request, HttpResponse.BodyHandlers.discarding()).statusCode();

            if (status >= 200 && status < 300) {
                cert.resetLinkStatus();
                log.info("Link valid for ID {}: {}", cert.getId(), url);
                success = true;
            } else {
                cert.recordLinkFailure();
                log.warn("Link broken (Status {}) for ID {}: {}", status, cert.getId(), url);
            }

        } catch (IllegalArgumentException e) {
            cert.recordLinkFailure();
            log.warn("Skipping malformed URL for ID {}: '{}'. Reason: {}", cert.getId(), url, e.getMessage());
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
            log.warn("Job interrupted while checking link for ID {}: {}", cert.getId(), url);
        } catch (IOException e) {
            cert.recordLinkFailure();
            log.error("Link unreachable (Network/IO) for ID {}: {}", cert.getId(), e.toString());
        }

        certificateTypeBusinessService.update(cert.getId(), cert);

        return success;
    }
}
