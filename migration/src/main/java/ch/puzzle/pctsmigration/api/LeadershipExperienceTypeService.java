package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.LeadershipExperienceTypesApi;
import org.openapitools.client.model.LeadershipExperienceTypeDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceTypeService {

    private final LeadershipExperienceTypesApi leadershipExperienceTypesApi;

    public LeadershipExperienceTypeService(LeadershipExperienceTypesApi leadershipExperienceTypesApi) {
        this.leadershipExperienceTypesApi = leadershipExperienceTypesApi;
    }

    public List<LeadershipExperienceTypeDto> getLeadershipExperienceTypes() {
        try {
            return this.leadershipExperienceTypesApi.getLeadershipExperienceTypes();
        } catch (ApiException e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), e.getMessage()));
        }
    }
}
