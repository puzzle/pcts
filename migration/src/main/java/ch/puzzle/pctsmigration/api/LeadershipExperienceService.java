package ch.puzzle.pctsmigration.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.LeadershipExperiencesApi;
import org.openapitools.client.model.LeadershipExperienceDto;
import org.openapitools.client.model.LeadershipExperienceInputDto;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceService extends CreationBase<LeadershipExperienceInputDto, LeadershipExperienceDto> {
    private final LeadershipExperiencesApi leadershipExperiencesApi;

    public LeadershipExperienceService(LeadershipExperiencesApi leadershipExperiencesApi) {
        this.leadershipExperiencesApi = leadershipExperiencesApi;
    }

    @Override
    protected LeadershipExperienceDto executeCreate(LeadershipExperienceInputDto dto) throws ApiException {
        return this.leadershipExperiencesApi.createLeadershipExperience(dto);
    }

    @Override
    protected void executeDelete(Long id) throws ApiException {
        this.leadershipExperiencesApi.deleteLeadershipExperience(id);
    }

    @Override
    protected Long extractId(LeadershipExperienceDto entity) {
        return entity.getId();
    }
}
