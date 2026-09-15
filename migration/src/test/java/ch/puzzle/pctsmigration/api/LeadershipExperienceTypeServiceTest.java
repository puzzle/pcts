package ch.puzzle.pctsmigration.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.LeadershipExperienceTypesApi;
import org.openapitools.client.model.LeadershipExperienceTypeDto;

@ExtendWith(MockitoExtension.class)
public class LeadershipExperienceTypeServiceTest {

    @Mock
    private LeadershipExperienceTypesApi leadershipExperienceTypesApi;

    @InjectMocks
    private LeadershipExperienceTypeService leadershipExperienceTypeService;

    @Test
    void getLeadershipExperienceTypes_returnsListFromApi() throws Exception {
        LeadershipExperienceTypeDto type1 = new LeadershipExperienceTypeDto();
        type1.setName("Soldat");

        LeadershipExperienceTypeDto type2 = new LeadershipExperienceTypeDto();
        type2.setName("Führungskurs");

        List<LeadershipExperienceTypeDto> expectedTypes = List.of(type1, type2);

        when(leadershipExperienceTypesApi.getLeadershipExperienceTypes()).thenReturn(expectedTypes);

        List<LeadershipExperienceTypeDto> actualTypes = leadershipExperienceTypeService.getLeadershipExperienceTypes();

        assertThat(actualTypes).isNotNull().hasSize(2).containsExactlyElementsOf(expectedTypes);
        verify(leadershipExperienceTypesApi, times(1)).getLeadershipExperienceTypes();
    }

    @Test
    @DisplayName("getLeadershipExperienceTypes should rethrow an `ApiException` if the API call fails")
    void getLeadershipExperienceTypes_whenApiThrowsException_throwsApiException() throws Exception {
        ApiException apiException = new ApiException("HTTP 500: Internal Server Error");
        when(leadershipExperienceTypesApi.getLeadershipExperienceTypes()).thenThrow(apiException);

        assertThatThrownBy(() -> leadershipExperienceTypeService.getLeadershipExperienceTypes())
                .isInstanceOf(MigrationException.class)
                .hasMessage("400 BAD_REQUEST");

        verify(leadershipExperienceTypesApi, times(1)).getLeadershipExperienceTypes();
    }
}
