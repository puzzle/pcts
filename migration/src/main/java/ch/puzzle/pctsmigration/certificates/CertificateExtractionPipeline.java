package ch.puzzle.pctsmigration.certificates;

import ch.puzzle.pctsmigration.extractor.ExtractionPipeline;
import ch.puzzle.pctsmigration.api.CertificateTypeService;
import ch.puzzle.pctsmigration.api.MemberService;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CertificateInputDto;
import org.openapitools.client.model.CertificateTypeDto;
import org.springframework.stereotype.Component;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class CertificateExtractionPipeline implements ExtractionPipeline<CertificateContextModel, CertificateWrapper, CertificateInputDto> {

    private final CertificateTypeService certificateTypeService;
    private final MemberService memberService;
    private final LevenshteinDistance distance = LevenshteinDistance.getDefaultInstance();

    public CertificateExtractionPipeline(CertificateTypeService certificateTypeService, MemberService memberService) {
        this.certificateTypeService = certificateTypeService;
        this.memberService = memberService;
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
                """.formatted(context.currentDate());
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Class<CertificateWrapper> entityClass() {
        return CertificateWrapper.class;
    }

    @Override
    public List<CertificateInputDto> mapToDto(String filename, CertificateWrapper wrapper) {
        if (wrapper == null || wrapper.items() == null) {
            return Collections.emptyList();
        }

        String abbreviation = extractAbbreviation(filename);

        return wrapper.items().stream()
                .map(aiResult -> createCertificateInputDto(abbreviation, aiResult))
                .toList();
    }

    private String extractAbbreviation(String filename) {
        if (filename.contains("_")) {
            return filename.split("_")[0].toUpperCase();
        }
        return null;
    }

    private CertificateInputDto createCertificateInputDto(String abbreviation, CertificateAiResultDto aiResult) {
        try {
            CertificateInputDto dto = new CertificateInputDto();
            dto.setMemberId(this.memberService.getMemberIdBy(abbreviation));
            dto.setCertificateTypeId(mapCertificateTypeId(aiResult.name(), aiResult.points()));
            dto.setValidUntil(null);
            dto.setComment(aiResult.comment());
            dto.setCompletedAt(aiResult.completedAt());
            return dto;
        } catch (ApiException e) {
            throw new IllegalStateException("Failed to map AI result to DTO: " + abbreviation, e);
        }
    }

    private Long mapCertificateTypeId(String name, BigDecimal points) throws ApiException {
        List<CertificateTypeDto> dtos = this.certificateTypeService.getCertificateTypes();

        return dtos.stream()
                .min(Comparator.comparingInt(dto -> calculateDistance(dto, name, points)))
                .map(CertificateTypeDto::getId)
                .orElse(null);
    }

    private int calculateDistance(CertificateTypeDto dto, String name, BigDecimal points) {
        int distanceOfName = this.distance.apply(dto.getName(), name);
        int distanceOfPoints = this.distance.apply(dto.getPoints().toString(), points.toString());
        return distanceOfName + distanceOfPoints;
    }
}
