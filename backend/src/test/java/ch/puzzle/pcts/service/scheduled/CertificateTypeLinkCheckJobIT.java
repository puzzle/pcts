package ch.puzzle.pcts.service.scheduled;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.head;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.*;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import ch.puzzle.pcts.util.IT;
import ch.puzzle.pcts.util.TestDataModels;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.net.URI;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@IT
class CertificateTypeLinkCheckJobIT {
    @Value("${wiremock.base-url}")
    private String wiremockBaseUrl;

    @Autowired
    private CertificateTypeLinkCheckJob cleanupJob;

    @Autowired
    private CertificateTypeBusinessService certificateTypeBusinessService;

    @AfterEach
    void tearDownWireMock() {
        WireMock.reset();
    }

    @Test
    void shouldMarkLinkAsValidWhenWireMockReturns200() {
        int port = URI.create(wiremockBaseUrl).getPort();
        WireMock.configureFor("localhost", port);

        stubFor(head(urlEqualTo("/valid-cert")).willReturn(aResponse().withStatus(200)));

        CertificateType cert = TestDataModels.CERT_TYPE_1;
        cert.setLink(wiremockBaseUrl + "/valid-cert");

        certificateTypeBusinessService.update(cert.getId(), cert);

        cleanupJob.validateCertificateLinks();

        CertificateType updatedCert = certificateTypeBusinessService.getById(cert.getId());
        assertNotEquals(null, updatedCert.getLinkLastCheckedAt());
        assertEquals(0L, updatedCert.getLinkErrorCount());
        assertTrue(updatedCert.isLinkValid());

        certificateTypeBusinessService.update(cert.getId(), TestDataModels.CERT_TYPE_1);
    }

    @Test
    void shouldMarkLinkAsValidWhenWireMockRedirectsToValidUrl() {
        int port = URI.create(wiremockBaseUrl).getPort();
        WireMock.configureFor("localhost", port);

        stubFor(head(urlEqualTo("/redirect-cert"))
                .willReturn(aResponse().withStatus(301).withHeader("Location", wiremockBaseUrl + "/target-cert")));

        stubFor(head(urlEqualTo("/target-cert")).willReturn(aResponse().withStatus(200)));

        CertificateType cert = TestDataModels.CERT_TYPE_1;
        cert.setLink(wiremockBaseUrl + "/redirect-cert");

        certificateTypeBusinessService.update(cert.getId(), cert);

        cleanupJob.validateCertificateLinks();

        CertificateType updatedCert = certificateTypeBusinessService.getById(cert.getId());
        assertNotEquals(null, updatedCert.getLinkLastCheckedAt());
        assertEquals(0L, updatedCert.getLinkErrorCount());
        assertTrue(updatedCert.isLinkValid());

        certificateTypeBusinessService.update(cert.getId(), TestDataModels.CERT_TYPE_1);
    }

    @Test
    void shouldIncrementErrorCountWhenWireMockReturns404() {
        int port = URI.create(wiremockBaseUrl).getPort();
        WireMock.configureFor("localhost", port);

        stubFor(head(urlEqualTo("/not-found-cert")).willReturn(aResponse().withStatus(404)));

        CertificateType cert = TestDataModels.CERT_TYPE_1;
        cert.setLink(wiremockBaseUrl + "/not-found-cert");

        certificateTypeBusinessService.update(cert.getId(), cert);
        cleanupJob.validateCertificateLinks();

        CertificateType updatedCert = certificateTypeBusinessService.getById(cert.getId());
        assertNotEquals(null, updatedCert.getLinkLastCheckedAt());

        assertEquals(1L, updatedCert.getLinkErrorCount());
        assertTrue(updatedCert.isLinkValid());

        certificateTypeBusinessService.update(cert.getId(), TestDataModels.CERT_TYPE_1);
    }
}
