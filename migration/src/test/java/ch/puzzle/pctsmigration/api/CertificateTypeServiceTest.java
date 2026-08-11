package ch.puzzle.pctsmigration.api;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificateTypesApi;
import org.openapitools.client.model.CertificateTypeDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CertificateTypeServiceTest {

    @Mock
    private CertificateTypesApi certificateTypesApi;

    @InjectMocks
    private CertificateTypeService certificateTypeService;

    @Test
    @DisplayName("getCertificateTypes should retrieve and return the list of certificate types from the API")
    void getCertificateTypes_returnsListFromApi() throws Exception {
        CertificateTypeDto type1 = new CertificateTypeDto();
        type1.setName("Scrum Master");

        CertificateTypeDto type2 = new CertificateTypeDto();
        type2.setName("Java Architect");

        List<CertificateTypeDto> expectedTypes = List.of(type1, type2);

        when(certificateTypesApi.getCertificateTypes()).thenReturn(expectedTypes);

        List<CertificateTypeDto> actualTypes = certificateTypeService.getCertificateTypes();

        assertThat(actualTypes)
                .isNotNull()
                .hasSize(2)
                .containsExactlyElementsOf(expectedTypes);

        verify(certificateTypesApi, times(1)).getCertificateTypes();
    }

    @Test
    @DisplayName("getCertificateTypes should rethrow an `ApiException` if the API call fails")
    void getCertificateTypes_whenApiThrowsException_throwsApiException() throws Exception {
        ApiException apiException = new ApiException("HTTP 500: Internal Server Error");
        when(certificateTypesApi.getCertificateTypes()).thenThrow(apiException);

        assertThatThrownBy(() -> certificateTypeService.getCertificateTypes())
                .isInstanceOf(ApiException.class)
                .hasMessage("HTTP 500: Internal Server Error");

        verify(certificateTypesApi, times(1)).getCertificateTypes();
    }
}