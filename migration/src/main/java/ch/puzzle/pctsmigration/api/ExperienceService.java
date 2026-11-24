package ch.puzzle.pctsmigration.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.ExperiencesApi;
import org.openapitools.client.model.ExperienceDto;
import org.openapitools.client.model.ExperienceInputDto;
import org.springframework.stereotype.Service;

@Service
public class ExperienceService extends ApiBase<ExperienceInputDto, ExperienceDto> {
    private final ExperiencesApi experiencesApi;

    public ExperienceService(ExperiencesApi experiencesApi) {
        this.experiencesApi = experiencesApi;
    }

    @Override
    protected ExperienceDto executeCreate(ExperienceInputDto dto) throws ApiException {
        return this.experiencesApi.createExperience(dto);
    }

    @Override
    protected void executeDelete(Long id) throws ApiException {
        this.experiencesApi.deleteExperience(id);
    }

    @Override
    protected Long extractId(ExperienceDto dto) {
        return dto.getId();
    }
}
