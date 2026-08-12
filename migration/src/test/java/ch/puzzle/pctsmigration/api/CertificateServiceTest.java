package ch.puzzle.pctsmigration.api;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificatesApi;
import org.openapitools.client.model.CertificateInputDto;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificatesApi certificatesApi;

    @InjectMocks
    private CertificateService certificateService;

    @Nested
    @DisplayName("create(List<CertificateInputDto>)")
    class CreateTests {

        @Test
        @DisplayName("Should call createCertificate for each DTO in the list in the correct order")
        void create_withMultipleDtos_callsApiForEachDto() throws Exception {
            CertificateInputDto dto1 = new CertificateInputDto();
            dto1.setComment("Zertifikat 1");

            CertificateInputDto dto2 = new CertificateInputDto();
            dto2.setComment("Zertifikat 2");

            List<CertificateInputDto> dtos = List.of(dto1, dto2);

            certificateService.create(dtos);

            InOrder inOrder = inOrder(certificatesApi);
            inOrder.verify(certificatesApi, times(1)).createCertificate(dto1);
            inOrder.verify(certificatesApi, times(1)).createCertificate(dto2);
            inOrder.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("Should not make an API call if the passed list is empty")
        void create_withEmptyList_doesNotCallApi() throws Exception {
            List<CertificateInputDto> emptyList = Collections.emptyList();

            certificateService.create(emptyList);

            verifyNoInteractions(certificatesApi);
        }

        @Test
        @DisplayName("Should terminate immediately and throw an ApiException if an API call fails")
        void create_whenApiThrowsException_throwsApiExceptionAndStopsProcessing() throws Exception {
            CertificateInputDto dto1 = new CertificateInputDto();
            CertificateInputDto dto2 = new CertificateInputDto();
            List<CertificateInputDto> dtos = List.of(dto1, dto2);

            doThrow(new ApiException("HTTP 400: Bad Request")).when(certificatesApi).createCertificate(dto1);

            assertThatThrownBy(() -> certificateService.create(dtos))
                    .isInstanceOf(ApiException.class)
                    .hasMessage("HTTP 400: Bad Request");

            verify(certificatesApi, times(1)).createCertificate(dto1);
        }
    }
}