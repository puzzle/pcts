package ch.puzzle.pctsmigration.leadershipexperience;

import ch.puzzle.pctsmigration.api.*;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import org.openapitools.client.model.LeadershipExperienceInputDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class LeadershipExperienceExtractionPipeline
        implements
            ExtractionPipeline<LeadershipExperienceContextModel, LeadershipExperienceWrapper, LeadershipExperienceInputDto> {
    private final static Logger logger = LoggerFactory.getLogger(LeadershipExperienceExtractionPipeline.class);

    private final LeadershipExperienceService leadershipExperienceService;

    public LeadershipExperienceExtractionPipeline(LeadershipExperienceService leadershipExperienceService) {
        this.leadershipExperienceService = leadershipExperienceService;
    }

    @Override
    public LeadershipExperienceContextModel fetchContext() {
        return new LeadershipExperienceContextModel(LocalDate.now());
    }

    @Override
    public String systemPrompt(LeadershipExperienceContextModel context) {
        return """
                You are a high-precision assistant for data extraction. Your task is to process parsed spreadsheet data and extract a LIST of certificate records into a strictly formatted JSON array.

                IMPORTANT EXTRACTION RULES:
                1. Output format: Return ONLY a valid JSON array with objects that conform to the requested schema. No conversation text may appear before or after the JSON.
                2. Each data row in the 'Zertifikat' column corresponds to exactly ONE certificate object in the resulting array.
                === CONTEXT ===
                Current date: %s
                """
                .formatted(context.currentDate());
    }

    @Override
    public List<String> tableNames() {
        return List.of("Berufsehrfahrung", "Berufsehrfahrungen");
    }

    @Override
    public Class<LeadershipExperienceWrapper> entityClass() {
        return LeadershipExperienceWrapper.class;
    }

    @Override
    public List<LeadershipExperienceInputDto> mapToDto(String filename, LeadershipExperienceWrapper wrapper) {
        return wrapper.items().stream().map(aiResult -> createLeadershipExperienceInputDto()).toList();
    }

    private LeadershipExperienceInputDto createLeadershipExperienceInputDto() {
        LeadershipExperienceInputDto dto = new LeadershipExperienceInputDto();
        return dto;
    }

    @Override
    public void create(List<LeadershipExperienceInputDto> dtos) {
        this.leadershipExperienceService.create(dtos);
    }
}
