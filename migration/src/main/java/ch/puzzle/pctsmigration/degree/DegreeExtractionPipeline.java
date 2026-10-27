package ch.puzzle.pctsmigration.degree;

import ch.puzzle.pctsmigration.api.DegreeService;
import ch.puzzle.pctsmigration.api.DegreeTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import ch.puzzle.pctsmigration.service.MatchingService;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.openapitools.client.model.DegreeInputDto;
import org.openapitools.client.model.DegreeTypeDto;
import org.springframework.stereotype.Component;

@Component
public class DegreeExtractionPipeline extends ExtractionPipeline<DegreeContextModel, DegreeWrapper, DegreeInputDto> {

    private final DegreeTypeService degreeTypeService;
    private final MemberService memberService;
    private final DegreeService degreeService;
    private final MatchingService matchingService;

    public DegreeExtractionPipeline(DegreeTypeService degreeTypeService, MemberService memberService,
                                    DegreeService degreeService, MatchingService matchingService) {
        this.degreeTypeService = degreeTypeService;
        this.memberService = memberService;
        this.degreeService = degreeService;
        this.matchingService = matchingService;
    }

    @Override
    public DegreeContextModel fetchContext() {
        return new DegreeContextModel(LocalDate.now());
    }

    @Override
    public String systemPrompt(DegreeContextModel context) {
        return """
                IMPORTANT EXTRACTION RULES:
                1. Each qualifying row must correspond to exactly ONE degree object in the output array.
                   To qualify for extraction, an entry must be located in the second column and have an assigned point value (indicated by a number in the 3rd, 4th, or 5th column of the same row).
                   Ignore all rows without numbers.
                2. Each degree can only be extracted exactly ONCE at most.
                === Context ===
                Current date: %s
                """
                .formatted(context.currentDate());
    }

    @Override
    public OdsParseConfig odsSheetParseConfig() {
        Predicate<String> isTableNotMasterAndNotContainingAlt = tableName -> tableName.matches("^[A-Z]\\d.*") && !tableName.contains("alt");
        return new OdsParseConfig(isTableNotMasterAndNotContainingAlt, "Ausbildung", true);
    }

    @Override
    public Class<DegreeWrapper> entityClass() {
        return DegreeWrapper.class;
    }

    @Override
    public List<DegreeInputDto> mapToDto(String filename, DegreeWrapper wrapper) {
        String abbreviation = extractAbbreviation(filename);
        return wrapper.items().stream().map(aiResult -> createDegreeInputDto(abbreviation, aiResult)).toList();
    }

    private DegreeInputDto createDegreeInputDto(String abbreviation, DegreeAiResultDto aiResult) {
        DegreeTypeDto matchedType = mapDegreeType(aiResult.name());
        DegreeInputDto dto = new DegreeInputDto();
        dto.setMemberId(this.memberService.getMemberIdBy(abbreviation));
        dto.setTypeId(matchedType.getId());
        dto.setComment(aiResult.comment());
        dto.setCompleted(true);
        dto.setStartDate(LocalDate.of(1970, 1, 1));
        dto.setName(matchedType.getName());
        return dto;
    }

    private DegreeTypeDto mapDegreeType(String name) {
        List<DegreeTypeDto> dtos = this.degreeTypeService.getDegreeTypes();
        Function<DegreeTypeDto, String> getTargetAttr = DegreeTypeDto::getName;

        return this.matchingService.match(dtos, name, getTargetAttr);
    }

    @Override
    public void create(List<DegreeInputDto> dtos) {
        this.degreeService.create(dtos);
    }
}
