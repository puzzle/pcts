package ch.puzzle.pctsmigration.leadershipexperience;

import ch.puzzle.pctsmigration.api.*;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.extractor.Pipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.openapitools.client.model.LeadershipExperienceInputDto;
import org.openapitools.client.model.LeadershipExperienceTypeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperienceExtractionPipeline extends Pipeline
        implements
            ExtractionPipeline<LeadershipExperienceContextModel, LeadershipExperienceWrapper, LeadershipExperienceInputDto> {
    private final static Logger logger = LoggerFactory.getLogger(LeadershipExperienceExtractionPipeline.class);

    private final LeadershipExperienceService leadershipExperienceService;
    private final LeadershipExperienceTypeService leadershipExperienceTypeService;
    private final MemberService memberService;
    private final LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();

    public LeadershipExperienceExtractionPipeline(LeadershipExperienceService leadershipExperienceService,
                                                  MemberService memberService,
                                                  LeadershipExperienceTypeService leadershipExperienceTypeService) {
        this.leadershipExperienceService = leadershipExperienceService;
        this.memberService = memberService;
        this.leadershipExperienceTypeService = leadershipExperienceTypeService;
    }

    @Override
    public LeadershipExperienceContextModel fetchContext() {
        return new LeadershipExperienceContextModel(LocalDate.now(),
                                                    List
                                                            .of(LeadershipExperienceTypeDto.LeadershipExperienceKindEnum.MILITARY_FUNCTION,
                                                                LeadershipExperienceTypeDto.LeadershipExperienceKindEnum.LEADERSHIP_TRAINING,
                                                                LeadershipExperienceTypeDto.LeadershipExperienceKindEnum.YOUTH_AND_SPORT));
    }

    @Override
    public String systemPrompt(LeadershipExperienceContextModel context) {
        return """
                You are a high-precision assistant for data extraction. Your task is to process parsed spreadsheet data and extract a LIST of c records into a strictly formatted JSON array.

                IMPORTANT EXTRACTION RULES:
                1. Output format: Return ONLY a valid JSON array with objects that conform to the requested schema. No conversation text may appear before or after the JSON.
                2. Each row of data in the ‘Führungserfahrung’ column corresponds to exactly ONE leadership experience object in the resulting array,
                   except for the columns whose names match the categories and which are shifted one column to the right.
                === CONTEXT ===
                Current date: %s
                Categories: %s
                """
                .formatted(context.currentDate(), context.kinds());
    }

    @Override
    public OdsParseConfig odsSheetParseConfig() {
        return new OdsParseConfig(List.of("M1 Project Manager"), "Führungserfahrung");
    }

    @Override
    public Class<LeadershipExperienceWrapper> entityClass() {
        return LeadershipExperienceWrapper.class;
    }

    @Override
    public List<LeadershipExperienceInputDto> mapToDto(String filename, LeadershipExperienceWrapper wrapper) {
        String abbreviation = extractAbbreviation(filename);
        return wrapper
                .items()
                .stream()
                .map(aiResult -> createLeadershipExperienceInputDto(abbreviation, aiResult))
                .toList();
    }

    private LeadershipExperienceInputDto createLeadershipExperienceInputDto(String abbreviation,
                                                                            LeadershipExperienceAiResultDto aiResult) {
        LeadershipExperienceInputDto dto = new LeadershipExperienceInputDto();
        dto.setMemberId(this.memberService.getMemberIdBy(abbreviation));
        dto.setLeadershipExperienceTypeId(mapLeadershipExperienceTypeId(aiResult.name()));
        dto.setComment(aiResult.comment());
        return dto;
    }

    private Long mapLeadershipExperienceTypeId(String name) {
        List<LeadershipExperienceTypeDto> dtos = this.leadershipExperienceTypeService.getLeadershipExperienceTypes();

        return dtos
                .stream()
                .min(Comparator.comparingInt(dto -> calculateDistance(dto.getName(), name)))
                .map(LeadershipExperienceTypeDto::getId)
                .orElseThrow();
    }

    @Override
    public void create(List<LeadershipExperienceInputDto> dtos) {
        this.leadershipExperienceService.create(dtos);
    }
}
