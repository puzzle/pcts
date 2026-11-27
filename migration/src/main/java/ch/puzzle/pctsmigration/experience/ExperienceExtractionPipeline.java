package ch.puzzle.pctsmigration.experience;

import ch.puzzle.pctsmigration.api.ExperienceService;
import ch.puzzle.pctsmigration.api.ExperienceTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import ch.puzzle.pctsmigration.service.MatchingService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import org.openapitools.client.model.ExperienceInputDto;
import org.openapitools.client.model.ExperienceTypeDto;
import org.springframework.stereotype.Component;

@Component
public class ExperienceExtractionPipeline
        extends
            ExtractionPipeline<ExperienceContextModel, ExperienceAiResultDto, ExperienceWrapper, ExperienceInputDto> {

    private final ExperienceService experienceService;
    private final ExperienceTypeService experienceTypeService;
    private final MemberService memberService;
    private final MatchingService matchingService;

    public ExperienceExtractionPipeline(ExperienceService experienceService,
                                        ExperienceTypeService experienceTypeService, MemberService memberService,
                                        MatchingService matchingService) {
        this.experienceService = experienceService;
        this.experienceTypeService = experienceTypeService;
        this.memberService = memberService;
        this.matchingService = matchingService;
    }

    @Override
    protected ExperienceWrapper createWrapper(List<ExperienceAiResultDto> items) {
        return new ExperienceWrapper(items);
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

        return new OdsParseConfig(isMatchingConvention, null, Map.of("Berufserfahrung", List.of("Praktika")), false);
    }

    @Override
    public Class<ExperienceWrapper> entityClass() {
        return ExperienceWrapper.class;
    }

    @Override
    public List<ExperienceInputDto> mapToDto(String filename, ExperienceWrapper wrapper) {
        String abbreviation = extractAbbreviation(filename);
        return wrapper.items().stream().map(aiResult -> createExperienceInputDto(abbreviation, aiResult)).toList();
    }

    private ExperienceInputDto createExperienceInputDto(String abbreviation, ExperienceAiResultDto aiResult) {
        ExperienceTypeDto matchedType = mapDegreeType(aiResult.type().getValue());

        ExperienceInputDto dto = new ExperienceInputDto();
        dto.setMemberId(this.memberService.getMemberIdBy(abbreviation));
        dto.setName(aiResult.name());
        dto.setEmployer(aiResult.employer());
        dto.setPercent(aiResult.percent());
        dto.setExperienceTypeId(matchedType.getId());
        dto.setComment(aiResult.comment());
        dto.setStartDate(aiResult.startDate());
        dto.setEndDate(aiResult.endDate());
        return dto;
    }

    private ExperienceTypeDto mapDegreeType(String name) {
        List<ExperienceTypeDto> dtos = this.experienceTypeService.getExperienceTypes();
        Function<ExperienceTypeDto, String> getTargetAttr = ExperienceTypeDto::getName;

        return this.matchingService.match(dtos, name, getTargetAttr);
    }

    @Override
    public void create(List<ExperienceInputDto> dtos) {
        this.experienceService.create(dtos);
    }
}
