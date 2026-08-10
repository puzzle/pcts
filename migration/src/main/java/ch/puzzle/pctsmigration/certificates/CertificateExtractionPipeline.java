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
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public String name() {
        return "certificates";
    }

    @Override
    public CertificateContextModel fetchContext() {
        return new CertificateContextModel(LocalDate.now());
    }

    @Override
    public String systemPrompt(CertificateContextModel context) {
        String currentDateInfo = context.currentDate().toString();

        return """
                You are a high-precision assistant for data extraction. Your task is to process parsed spreadsheet data and extract a LIST of certificate records into a strictly formatted JSON array.
                
                IMPORTANT EXTRACTION RULES:
                1. Output format: Return ONLY a valid JSON array with objects that conform to the requested schema. No conversation text may appear before or after the JSON.
                2. Each data row in the 'Zertifikat' column corresponds to exactly ONE certificate object in the resulting array.
                === CONTEXT ===
                Current date: %s
                """.formatted(currentDateInfo
        );
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
    public Function<CertificateWrapper, List<CertificateInputDto>> mapToDto(String abbreviation) {
        return wrapper -> {
            if (wrapper == null) {
                return Collections.emptyList();
            }

            return wrapper.items().stream()
                    .map(aiResult -> {
                        CertificateInputDto dto = new CertificateInputDto();
                        try {
                            dto.setMemberId(this.memberService.getMemberIdBy(abbreviation));
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            dto.setCertificateTypeId(mapCertificateTypeId(aiResult.name(), aiResult.points()));
                        } catch (ApiException e) {
                            throw new RuntimeException(e);
                        }
                        dto.setValidUntil(null);
                        dto.setComment(aiResult.comment());
                        dto.setCompletedAt(aiResult.completedAt());

                        return dto;
                    }).collect(Collectors.toList());
        };
    }

    private Long mapCertificateTypeId(String name, BigDecimal points) throws ApiException {
        List<CertificateTypeDto> dtos = this.certificateTypeService.getCertificateTypes();

        Long bestId = null;
        int minDistance = Integer.MAX_VALUE;

        for (CertificateTypeDto dto : dtos) {
            Integer distanceOfName = this.distance.apply(dto.getName(), name);
            Integer distanceOfPoints = this.distance.apply(dto.getPoints().toString(), points.toString());
            Integer distanceOfDto = distanceOfName + distanceOfPoints;

            if (distanceOfDto < minDistance) {
                minDistance = distanceOfDto;
                bestId = dto.getId();
            }
        }

        return bestId;
    }
}
