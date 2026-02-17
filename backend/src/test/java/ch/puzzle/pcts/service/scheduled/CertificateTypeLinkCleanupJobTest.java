package ch.puzzle.pcts.service.scheduled;

import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.service.business.CertificateTypeBusinessService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateTypeLinkCleanupJobTest {

    @Mock
    private CertificateTypeBusinessService businessService;

    @InjectMocks
    private CertificateTypeLinkCleanupJob cleanupJob;

    @DisplayName("Should skip when the certificateType link is either null or blank")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "  ", "\t", "\n" })
    void validateCertificateLinksShouldSkipWhenUrlIsInvalid(String invalidUrl) {
        CertificateType cert = mock(CertificateType.class);
        when(cert.getLink()).thenReturn(invalidUrl);
        when(businessService.findAllWhereLinkIsNotNull()).thenReturn(List.of(cert));

        cleanupJob.validateCertificateLinks();

        verify(businessService, never()).create(any());

        verify(cert, never()).recordLinkFailure();
        verify(cert, never()).resetLinkStatus();
    }
}
