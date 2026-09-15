package ch.puzzle.pctsmigration.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.LeadershipExperiencesApi;
import org.openapitools.client.model.LeadershipExperienceDto;
import org.openapitools.client.model.LeadershipExperienceInputDto;

@ExtendWith(MockitoExtension.class)
public class LeadershipExperienceServiceTest {

    @Mock
    private LeadershipExperiencesApi leadershipExperiencesApi;

    @InjectMocks
    private LeadershipExperienceService leadershipExperienceService;

    @Test
    void testExecuteCreate() throws ApiException {
        LeadershipExperienceInputDto input = new LeadershipExperienceInputDto();
        LeadershipExperienceDto expectedOutput = new LeadershipExperienceDto();
        when(leadershipExperiencesApi.createLeadershipExperience(input)).thenReturn(expectedOutput);

        LeadershipExperienceDto result = leadershipExperienceService.executeCreate(input);

        assertEquals(expectedOutput, result);
        verify(leadershipExperiencesApi).createLeadershipExperience(input);
    }

    @Test
    void testExecuteDelete() throws ApiException {
        Long id = 123L;

        leadershipExperienceService.executeDelete(id);

        verify(leadershipExperiencesApi).deleteLeadershipExperience(id);
    }

    @Test
    void testExtractId() {
        LeadershipExperienceDto entity = mock(LeadershipExperienceDto.class);
        when(entity.getId()).thenReturn(99L);

        Long result = leadershipExperienceService.extractId(entity);

        assertEquals(99L, result);
    }
}
