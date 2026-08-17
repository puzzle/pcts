package ch.puzzle.pctsmigration.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificatesApi;
import org.openapitools.client.model.CertificateDto;
import org.openapitools.client.model.CertificateInputDto;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificatesApi certificatesApi;

    @InjectMocks
    private CertificateService certificateService;

    private CertificateInputDto inputDto1;
    private CertificateInputDto inputDto2;
    private CertificateDto createdDto1;
    private CertificateDto createdDto2;

    @BeforeEach
    void setUp() {
        inputDto1 = new CertificateInputDto();
        inputDto1.setComment("cert 1");

        inputDto2 = new CertificateInputDto();
        inputDto2.setComment("cert 2");

        createdDto1 = new CertificateDto(101L);
        createdDto2 = new CertificateDto(102L);
    }

    @Test
    void testCreate_Success() throws ApiException {
        List<CertificateInputDto> dtoList = Arrays.asList(inputDto1, inputDto2);

        when(certificatesApi.createCertificate(inputDto1)).thenReturn(createdDto1);
        when(certificatesApi.createCertificate(inputDto2)).thenReturn(createdDto2);

        certificateService.create(dtoList);

        verify(certificatesApi, times(1)).createCertificate(inputDto1);
        verify(certificatesApi, times(1)).createCertificate(inputDto2);
        verify(certificatesApi, never()).deleteCertificate(any());
    }

    @Test
    void testCreate_ThrowsApiException_TriggersRollback() throws ApiException {
        List<CertificateInputDto> dtoList = Arrays.asList(inputDto1, inputDto2);

        when(certificatesApi.createCertificate(inputDto1)).thenReturn(createdDto1);
        when(certificatesApi.createCertificate(inputDto2)).thenThrow(new ApiException("Simulated API Error"));

        MigrationException exception = assertThrows(MigrationException.class, () -> {
            certificateService.create(dtoList);
        });

        assertEquals("Migration aborted. Reason: Simulated API Error", exception.getError().message());

        verify(certificatesApi, times(1)).createCertificate(inputDto1);
        verify(certificatesApi, times(1)).createCertificate(inputDto2);

        verify(certificatesApi, times(1)).deleteCertificate(101L);
        verify(certificatesApi, never()).deleteCertificate(102L);
    }

    @Test
    void testCreate_RollbackFails_LogsErrorAndStillThrowsMigrationException() throws ApiException {
        List<CertificateInputDto> dtoList = Arrays.asList(inputDto1, inputDto2);

        when(certificatesApi.createCertificate(inputDto1)).thenReturn(createdDto1);
        when(certificatesApi.createCertificate(inputDto2)).thenThrow(new ApiException("Simulated API Error"));

        doThrow(new ApiException("Rollback failed")).when(certificatesApi).deleteCertificate(101L);

        MigrationException exception = assertThrows(MigrationException.class, () -> {
            certificateService.create(dtoList);
        });

        assertEquals("Migration aborted. Reason: Simulated API Error", exception.getError().message());

        verify(certificatesApi, times(1)).createCertificate(inputDto1);
        verify(certificatesApi, times(1)).createCertificate(inputDto2);
        verify(certificatesApi, times(1)).deleteCertificate(101L);
    }
}