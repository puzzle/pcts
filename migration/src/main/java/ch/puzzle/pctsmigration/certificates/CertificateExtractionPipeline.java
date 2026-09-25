package ch.puzzle.pctsmigration.certificates;

import ch.puzzle.pctsmigration.api.CertificateService;
import ch.puzzle.pctsmigration.api.CertificateTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.ods.OdsParseConfig;
import ch.puzzle.pctsmigration.service.MatchingService;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import org.openapitools.client.model.CertificateInputDto;
import org.openapitools.client.model.CertificateTypeDto;
import org.springframework.stereotype.Component;

@Component
public class CertificateExtractionPipeline
        extends
            ExtractionPipeline<CertificateContextModel, CertificateWrapper, CertificateInputDto> {

    private final CertificateTypeService certificateTypeService;
    private final MemberService memberService;
    private final CertificateService certificateService;
    private final MatchingService matchingService;

    public CertificateExtractionPipeline(CertificateTypeService certificateTypeService, MemberService memberService,
                                         CertificateService certificateService, MatchingService matchingService) {
        this.certificateTypeService = certificateTypeService;
        this.memberService = memberService;
        this.certificateService = certificateService;
        this.matchingService = matchingService;
    }

    @Override
    public CertificateContextModel fetchContext() {
        return new CertificateContextModel(LocalDate.now());
    }

    @Override
    public String systemPrompt(CertificateContextModel context) {
        return """
                IMPORTANT EXTRACTION RULES:
                1. Each data row in the 'Zertifikat' column corresponds to exactly ONE certificate object in the resulting array.
                === CONTEXT ===
                Current date: %s
                """
                .formatted(context.currentDate());
    }

    @Override
    public OdsParseConfig odsSheetParseConfig() {
        Predicate<String> isCertificateTable = tableName -> List.of("Zertifikat", "Zertifikate").contains(tableName);
        return new OdsParseConfig(isCertificateTable, null, false);
    }

    @Override
    public Class<CertificateWrapper> entityClass() {
        return CertificateWrapper.class;
    }

    @Override
    public List<CertificateInputDto> mapToDto(String filename, CertificateWrapper wrapper) {
        String abbreviation = extractAbbreviation(filename);
        return wrapper.items().stream().map(aiResult -> createCertificateInputDto(abbreviation, aiResult)).toList();
    }

    private CertificateInputDto createCertificateInputDto(String abbreviation, CertificateAiResultDto aiResult) {
        CertificateInputDto dto = new CertificateInputDto();
        dto.setMemberId(this.memberService.getMemberIdBy(abbreviation));
        dto.setCertificateTypeId(mapCertificateTypeId(aiResult.name()));
        dto.setValidUntil(null);
        dto.setComment(aiResult.comment());
        dto.setCompletedAt(aiResult.completedAt());
        return dto;
    }

    private Long mapCertificateTypeId(String name) {
        List<String> dtoNames = this.certificateTypeService
                .getCertificateTypes()
                .stream()
                .map(CertificateTypeDto::getName)
                .toList();

        String closestName = this.matchingService.match(dtoNames, name);

        for (CertificateTypeDto dto : this.certificateTypeService.getCertificateTypes()) {
            if (dto.getName().equals(closestName)) {
                return dto.getId();
            }
        }
        return -1L;
    }

    @Override
    public void create(List<CertificateInputDto> dtos) {
        this.certificateService.create(dtos);
    }
}
