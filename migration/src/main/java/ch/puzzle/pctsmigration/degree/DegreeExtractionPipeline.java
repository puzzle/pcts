package ch.puzzle.pctsmigration.degree;

import ch.puzzle.pctsmigration.api.DegreeService;
import ch.puzzle.pctsmigration.api.DegreeTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import ch.puzzle.pctsmigration.service.MatchingService;
import org.openapitools.client.model.DegreeInputDto;
import org.openapitools.client.model.DegreeTypeDto;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class DegreeExtractionPipeline
        extends
            ExtractionPipeline<DegreeContextModel, DegreeWrapper, DegreeInputDto> {

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
                1. Each data row in the '' column corresponds to exactly ONE degree object in the resulting array.
                === Context ===
                Current date: %s
                """
                .formatted(context.currentDate());
    }

    @Override
    public OdsParseConfig odsSheetParseConfig() {
        Predicate<String> isDegreeTable = tableName -> List.of("Ausbildung", "Ausbildungen").contains(tableName);
        return new OdsParseConfig(isDegreeTable, null, false);
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
        DegreeInputDto dto = new DegreeInputDto();
        return dto;
    }

    private Long mapDegreeTypeId(String name) {
        List<DegreeTypeDto> dtos = this.degreeTypeService.getDegreeTypes();
        Function<DegreeTypeDto, String> getTargetAttr = DegreeTypeDto::getName;

        return this.matchingService.match(dtos, name, getTargetAttr).getId();
    }

    @Override
    public void create(List<DegreeInputDto> dtos) {
        this.degreeService.create(dtos);
    }
}
