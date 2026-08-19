package ch.puzzle.pctsmigration.certificates;

import ch.puzzle.pctsmigration.api.CertificateService;
import ch.puzzle.pctsmigration.api.CertificateTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.openapitools.client.model.CertificateInputDto;
import org.openapitools.client.model.CertificateTypeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class CertificateExtractionPipeline
        implements
            ExtractionPipeline<CertificateContextModel, CertificateWrapper, CertificateInputDto> {
    private final static Logger logger = LoggerFactory.getLogger(CertificateExtractionPipeline.class);

    private final CertificateTypeService certificateTypeService;
    private final MemberService memberService;
    private final CertificateService certificateService;
    private final LevenshteinDistance levenshtein = LevenshteinDistance.getDefaultInstance();

    public CertificateExtractionPipeline(CertificateTypeService certificateTypeService, MemberService memberService,
                                         CertificateService certificateService) {
        this.certificateTypeService = certificateTypeService;
        this.memberService = memberService;
        this.certificateService = certificateService;
    }

    @Override
    public CertificateContextModel fetchContext() {
        return new CertificateContextModel(LocalDate.now());
    }

    @Override
    public String systemPrompt(CertificateContextModel context) {
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
        return List.of("Zertifikat", "Zertifikate");
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

    private String extractAbbreviation(String filename) {
        if (filename.contains("_")) {
            return filename.split("_")[0].toUpperCase();
        }
        throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                               "Invalid filename: can not extract abbreviation " + filename));
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
        List<CertificateTypeDto> dtos = this.certificateTypeService.getCertificateTypes();

        return dtos
                .stream()
                .min(Comparator.comparingInt(dto -> calculateDistance(dto, name)))
                .map(CertificateTypeDto::getId)
                .orElse(null);
    }

    private Integer calculateDistance(CertificateTypeDto dto, String name) {
        Integer distance = this.levenshtein.apply(dto.getName(), name);
        logger.info("Input name: {}, Actual name: {}, Distance: {}", name, dto.getName(), distance);

        return distance;
    }

    @Override
    public void create(List<CertificateInputDto> dtos) {
        this.certificateService.create(dtos);
    }
}
