package ch.puzzle.pctsmigration.leadershipexperience;

import ch.puzzle.pctsmigration.api.*;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.openapitools.client.model.LeadershipExperienceInputDto;
import org.openapitools.client.model.LeadershipExperienceTypeDto;
import org.springframework.stereotype.Component;

@Component
public class LeadershipExperienceExtractionPipeline
        extends
            ExtractionPipeline<LeadershipExperienceContextModel, LeadershipExperienceWrapper, LeadershipExperienceInputDto> {

    private final LeadershipExperienceService leadershipExperienceService;
    private final LeadershipExperienceTypeService leadershipExperienceTypeService;
    private final MemberService memberService;

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
                IMPORTANT EXTRACTION RULES:
                1. Each qualifying row must correspond to exactly ONE leadership experience object in the output array.
                   Skip the main categories in the first column.
                   To qualify for extraction, an entry must be located in the second column and have an assigned point value (indicated by a number in the 3rd, 4th, or 5th column of the same row).
                   Ignore all rows without numbers.
                === CONTEXT ===
                Current date: %s
                Categories: %s
                """
                .formatted(context.currentDate(), context.kinds());
    }

    @Override
    public OdsParseConfig odsSheetParseConfig() {
        return new OdsParseConfig(List.of("M2 Division Manager"), "Führungserfahrung");
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
