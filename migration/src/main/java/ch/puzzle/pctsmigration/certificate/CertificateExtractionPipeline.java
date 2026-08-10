package ch.puzzle.pctsmigration.certificate;

import ch.puzzle.pctsmigration.ExtractionPipeline;
import ch.puzzle.pctsmigration.service.pcts.CertificateTypeService;
import ch.puzzle.pctsmigration.service.pcts.MemberService;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CertificateInputDto;
import org.openapitools.client.model.CertificateTypeDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CertificateExtractionPipeline implements ExtractionPipeline<CertificateContextModel, CertificateWrapper, CertificateInputDto> {
    private final CertificateTypeService certificateTypeService;
    private final MemberService memberService;

    public CertificateExtractionPipeline(CertificateTypeService certificateTypeService, MemberService memberService) {
        this.certificateTypeService = certificateTypeService;
        this.memberService = memberService;
    }

    @Override
    public String name() {
        return "certificates";
    }

    @Override
    public CertificateContextModel fetchContext() throws ApiException {
        List<CertificateTypeDto> types = this.certificateTypeService.getCertificateTypes();
        LocalDate currentDate = LocalDate.now();

        return new CertificateContextModel(types, currentDate);
    }

    @Override
    public String systemPrompt(CertificateContextModel context) {
        String certificatesInfo = (context.types() == null || context.types().isEmpty())
                ? "No known certificates in context."
                : context.types().stream()
                .map(CertificateTypeDto::toString)
                .collect(Collectors.joining("\n---\n"));

        String currentDateInfo = context.currentDate() != null ? context.currentDate().toString() : "Unknown";

        return """
        You are a highly precise data extraction assistant. Your task is to process parsed spreadsheet data and extract a LIST of certificate records into a strict JSON array.

        CRITICAL EXTRACTION RULES:
        1. Output Format: Respond ONLY with a valid JSON array of objects matching the requested schema. No conversational text before or after the JSON.
        2. Each data row under the 'Zertifikat' column represents exactly ONE certificate object in the resulting array.
        3. The name of a certificate is always listed in the "Zertifikat" column
        4. Points are calculated in the input as follows: There are columns for the possible point values (0, 0.5, 1, 1.5, 2). The actual point value for a row is indicated by a '1' in the corresponding column. For example, if a row contains a '1' in the '0.5' column, this means that this certificate is worth 0.5 points.
        NEVER RETURN EMPTY OUTPUT OTHERWISE RETURN DUMMY DATA
        
        === CONTEXT ===
        Current Date: %s
        
        Known Certificate Types:
        %s
        """.formatted(
                currentDateInfo,
                certificatesInfo
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
            if (wrapper ==  null) {
                return Collections.emptyList();
            }

            return wrapper.items().stream()
                    .map(aiResult -> {
                        CertificateInputDto dto = new CertificateInputDto();
                        dto.setMemberId(this.memberService.getMemberIdBy(abbreviation));
                        dto.setCertificateTypeId(aiResult.certificateTypeId());
                        dto.setValidUntil(null);
                        dto.setComment(aiResult.comment());
                        dto.setCompletedAt(aiResult.completedAt());

                        return dto;
                    }).collect(Collectors.toList());
        };
    }
}
