package ch.puzzle.pctsmigration.experience;

import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.ExperienceInputDto;
import org.springframework.stereotype.Component;

@Component
public class ExperienceExtractionPipeline
        extends
            ExtractionPipeline<ExperienceContextModel, ExperienceWrapper, ExperienceInputDto> {
    @Override
    public ExperienceContextModel fetchContext() {
        return null;
    }

    @Override
    public String systemPrompt(ExperienceContextModel context) {
        return "";
    }

    @Override
    public OdsParseConfig odsSheetParseConfig() {
        return null;
    }

    @Override
    public Class<ExperienceWrapper> entityClass() {
        return null;
    }

    @Override
    public List<ExperienceInputDto> mapToDto(String filename, ExperienceWrapper wrapper) {
        return List.of();
    }

    @Override
    public void create(List<ExperienceInputDto> dtos) throws ApiException {

    }
}
