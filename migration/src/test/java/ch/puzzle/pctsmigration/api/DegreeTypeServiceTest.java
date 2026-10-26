package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.MigrationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DegreeTypesApi;
import org.openapitools.client.model.DegreeTypeDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DegreeTypeServiceTest {

    @Mock
    private DegreeTypesApi degreeTypesApi;

    @InjectMocks
    private DegreeTypeService degreeTypeService;

    @Test
    @DisplayName("getDegreeTypes should retrieve and return the list of degree types from the API")
    void getDegreeTypes_returnsListFromApi() throws Exception {
        DegreeTypeDto type1 = new DegreeTypeDto();
        type1.setName("Smth");

        DegreeTypeDto type2 = new DegreeTypeDto();
        type2.setName("Smth else");

        List<DegreeTypeDto> expectedTypes = List.of(type1, type2);

        when(degreeTypesApi.getDegreeType()).thenReturn(expectedTypes);

        List<DegreeTypeDto> actualTypes = degreeTypeService.getDegreeTypes();

        assertThat(actualTypes).isNotNull().hasSize(2).containsExactlyElementsOf(expectedTypes);

        verify(degreeTypesApi, times(1)).getDegreeType();
    }

    @Test
    @DisplayName("getDegreeTypes should rethrow an `ApiException` if the API call fails")
    void getDegreeTypes_whenApiThrowsException_throwsApiException() throws Exception {
        ApiException apiException = new ApiException("HTTP 500: Internal Server Error");
        when(degreeTypesApi.getDegreeType()).thenThrow(apiException);

        assertThatThrownBy(() -> degreeTypeService.getDegreeTypes())
                .isInstanceOf(MigrationException.class)
                .hasMessage("HTTP 500: Internal Server Error");

        verify(degreeTypesApi, times(1)).getDegreeType();
    }
}
