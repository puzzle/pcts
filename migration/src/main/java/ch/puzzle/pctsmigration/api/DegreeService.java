package ch.puzzle.pctsmigration.api;

import org.openapitools.client.ApiException;
import org.openapitools.client.api.DegreesApi;
import org.openapitools.client.model.DegreeDto;
import org.openapitools.client.model.DegreeInputDto;
import org.springframework.stereotype.Service;

@Service
public class DegreeService extends ApiBase<DegreeInputDto, DegreeDto> {
    private final DegreesApi degreesApi;

    public DegreeService(DegreesApi degreesApi) {
        this.degreesApi = degreesApi;
    }

    @Override
    protected DegreeDto executeCreate(DegreeInputDto dto) throws ApiException {
        return this.degreesApi.createNew(dto);
    }

    @Override
    protected void executeDelete(Long id) throws ApiException {
        this.degreesApi.delete(id);
    }

    @Override
    protected Long extractId(DegreeDto dto) {
        return dto.getId();
    }
}
