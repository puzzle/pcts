package ch.puzzle.pctsmigration.degree;

import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.DegreeInputDto;

import java.util.List;

public class DegreeExtractionPipeline
        extends
            ExtractionPipeline<DegreeContextModel, DegreeWrapper, DegreeInputDto> {

    @Override
    public DegreeContextModel fetchContext() {
        return null;
    }

    @Override
    public String systemPrompt(DegreeContextModel context) {
        return "";
    }

    @Override
    public OdsParseConfig odsSheetParseConfig() {
        return null;
    }

    @Override
    public Class<DegreeWrapper> entityClass() {
        return null;
    }

    @Override
    public List<DegreeInputDto> mapToDto(String filename, DegreeWrapper wrapper) {
        return List.of();
    }

    @Override
    public void create(List<DegreeInputDto> dtos) throws ApiException {

    }
}
