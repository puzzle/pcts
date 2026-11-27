package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.ExperienceTypesApi;
import org.openapitools.client.model.ExperienceTypeDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypeService {

    private final ExperienceTypesApi experienceTypesApi;

    public ExperienceTypeService(ExperienceTypesApi experienceTypesApi) {
        this.experienceTypesApi = experienceTypesApi;
    }

    public List<ExperienceTypeDto> getExperienceTypes() {
        try {
            return this.experienceTypesApi.getExperienceTypes();
        } catch (ApiException e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), e.getMessage()));
        }
    }
}
