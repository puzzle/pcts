package ch.puzzle.pctsmigration.api;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.DegreeTypesApi;
import org.openapitools.client.model.DegreeTypeDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DegreeTypeService {

    private final DegreeTypesApi degreeTypesApi;

    public DegreeTypeService(DegreeTypesApi degreeTypesApi) {
        this.degreeTypesApi = degreeTypesApi;
    }

    public List<DegreeTypeDto> getDegreeTypes() {
        try {
            return this.degreeTypesApi.getDegreeType();
        } catch (ApiException e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), e.getMessage()));
        }
    }
}
