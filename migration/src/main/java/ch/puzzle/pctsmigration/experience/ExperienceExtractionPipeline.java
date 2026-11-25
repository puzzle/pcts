package ch.puzzle.pctsmigration.experience;

import ch.puzzle.pctsmigration.api.ExperienceService;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.openapitools.client.model.ExperienceInputDto;
import org.springframework.stereotype.Component;

@Component
public class ExperienceExtractionPipeline
        extends
            ExtractionPipeline<ExperienceContextModel, ExperienceWrapper, ExperienceInputDto> {

    private final ExperienceService experienceService;

    public ExperienceExtractionPipeline(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @Override
    public ExperienceContextModel fetchContext() {
        return new ExperienceContextModel(LocalDate.now());
    }

    @Override
    public String systemPrompt(ExperienceContextModel context) {
        return """
                IMPORTANT EXTRACTION RULES:

                === Context ===
                Current date: %s
                """.formatted(context.currentDate());
    }

    @Override
    public OdsParseConfig odsSheetParseConfig() {
        Predicate<String> isMatchingConvention = tableName -> tableName.equals("Berufserfahrung");

        return new OdsParseConfig(isMatchingConvention, "", Map.of("Berufserfahrung", List.of("Praktika")), false);
    }

    @Override
    public Class<ExperienceWrapper> entityClass() {
        return ExperienceWrapper.class;
    }

    @Override
    public List<ExperienceInputDto> mapToDto(String filename, ExperienceWrapper wrapper) {
        return List.of();
    }

    @Override
    public void create(List<ExperienceInputDto> dtos) {
        this.experienceService.create(dtos);
    }
}
