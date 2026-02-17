package ch.puzzle.pcts.service.scheduled;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.head;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.*;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.ExamType;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import ch.puzzle.pcts.util.IT;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@IT
class CertificateTypeLinkCleanupJobIT {
    @Value("${wiremock.base-url}")
    private String wiremockBaseUrl;

    @Autowired
    private CertificateTypeLinkCleanupJob cleanupJob;

    @Autowired
    private CertificateTypeBusinessService certificateTypeBusinessService;

    @Test
    void shouldMarkLinkAsValidWhenWireMockReturns200() {
        int port = URI.create(wiremockBaseUrl).getPort();
        WireMock.configureFor("localhost", port);

        stubFor(head(urlEqualTo("/valid-cert")).willReturn(aResponse().withStatus(200)));

        CertificateType cert = new CertificateType();
        cert.setName("Test Certificate");
        cert.setPoints(BigDecimal.ONE);
        cert.setCertificateKind(CertificateKind.CERTIFICATE);
        cert.setPublisher("Test Publisher");
        cert.setExamType(ExamType.PRACTICAL);
        cert.setLink(wiremockBaseUrl + "/valid-cert");
        cert.setTags(new java.util.HashSet<>());

        certificateTypeBusinessService.create(cert);

        cleanupJob.validateCertificateLinks();

        CertificateType updatedCert = certificateTypeBusinessService.getById(cert.getId());
        assertNotEquals(null, updatedCert.getLinkLastCheckedAt());
        assertEquals(0L, updatedCert.getLinkErrorCount());
        assertTrue(updatedCert.isLinkValid());
    }

    @Test
    void shouldMarkLinkAsValidWhenWireMockRedirectsToValidUrl() {
        int port = URI.create(wiremockBaseUrl).getPort();
        WireMock.configureFor("localhost", port);

        stubFor(head(urlEqualTo("/redirect-cert"))
                .willReturn(aResponse().withStatus(301).withHeader("Location", wiremockBaseUrl + "/target-cert")));

        stubFor(head(urlEqualTo("/target-cert")).willReturn(aResponse().withStatus(200)));

        CertificateType cert = new CertificateType();
        cert.setName("Redirect Certificate");
        cert.setPoints(BigDecimal.ONE);
        cert.setCertificateKind(CertificateKind.CERTIFICATE);
        cert.setPublisher("Test Publisher");
        cert.setExamType(ExamType.PRACTICAL);
        cert.setTags(new java.util.HashSet<>());
        cert.setLink(wiremockBaseUrl + "/redirect-cert");
        certificateTypeBusinessService.create(cert);

        cleanupJob.validateCertificateLinks();

        CertificateType updatedCert = certificateTypeBusinessService.getById(cert.getId());
        assertNotEquals(null, updatedCert.getLinkLastCheckedAt());
        assertEquals(0L, updatedCert.getLinkErrorCount());
        assertTrue(updatedCert.isLinkValid());
    }

    @Test
    void shouldIncrementErrorCountWhenWireMockReturns404() {
        int port = URI.create(wiremockBaseUrl).getPort();
        WireMock.configureFor("localhost", port);

        stubFor(head(urlEqualTo("/not-found-cert")).willReturn(aResponse().withStatus(404)));

        CertificateType cert = new CertificateType();
        cert.setName("Broken Certificate");
        cert.setPoints(BigDecimal.ONE);
        cert.setCertificateKind(CertificateKind.CERTIFICATE);
        cert.setPublisher("Test Publisher");
        cert.setExamType(ExamType.PRACTICAL);
        cert.setTags(new java.util.HashSet<>());
        cert.setLink(wiremockBaseUrl + "/not-found-cert");
        certificateTypeBusinessService.create(cert);

        cleanupJob.validateCertificateLinks();

        CertificateType updatedCert = certificateTypeBusinessService.getById(cert.getId());
        assertNotEquals(null, updatedCert.getLinkLastCheckedAt());

        assertEquals(1L, updatedCert.getLinkErrorCount());
        assertTrue(updatedCert.isLinkValid());
    }
}
