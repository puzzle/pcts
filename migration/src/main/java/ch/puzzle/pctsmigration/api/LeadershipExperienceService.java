package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.LeadershipExperiencesApi;
import org.openapitools.client.model.LeadershipExperienceDto;
import org.openapitools.client.model.LeadershipExperienceInputDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceService {
    private final LeadershipExperiencesApi leadershipExperiencesApi;
    private final Logger logger = LoggerFactory.getLogger(LeadershipExperienceService.class);

    public LeadershipExperienceService(LeadershipExperiencesApi leadershipExperiencesApi) {
        this.leadershipExperiencesApi = leadershipExperiencesApi;
    }

    public void create(List<LeadershipExperienceInputDto> dtos) {
        List<Long> createdIds = new ArrayList<>();

        for (LeadershipExperienceInputDto dto : dtos) {
            try {
                LeadershipExperienceDto created = this.leadershipExperiencesApi.createLeadershipExperience(dto);
                createdIds.add(created.getId());
            } catch (ApiException e) {
                rollbackCreatedLeadershipExperiences(createdIds);
                throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                        "Migration aborted. Reason: " + e.getMessage()));
            }
        }
    }

    private void rollbackCreatedLeadershipExperiences(List<Long> createdIds) {
        for (Long id : createdIds) {
            try {
                this.leadershipExperiencesApi.deleteLeadershipExperience(id);
            } catch (ApiException rollbackException) {
                this.logger.error("Rollback failed for ID {}", id, rollbackException);
            }
        }
    }
}
