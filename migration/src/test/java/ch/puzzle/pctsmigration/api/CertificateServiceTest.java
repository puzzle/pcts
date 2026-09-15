package ch.puzzle.pctsmigration.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    void testExecuteCreate() throws ApiException {
        CertificateInputDto input = new CertificateInputDto();
        CertificateDto expectedOutput = new CertificateDto();
        when(certificatesApi.createCertificate(input)).thenReturn(expectedOutput);

        CertificateDto result = certificateService.executeCreate(input);

        assertEquals(expectedOutput, result);
        verify(certificatesApi).createCertificate(input);
    }

    @Test
    void testExecuteDelete() throws ApiException {
        Long id = 123L;

        certificateService.executeDelete(id);

        verify(certificatesApi).deleteCertificate(id);
    }

    @Test
    void testExtractId() {
        CertificateDto entity = mock(CertificateDto.class);
        when(entity.getId()).thenReturn(99L);

        Long result = certificateService.extractId(entity);

        assertEquals(99L, result);
    }
}