package ch.puzzle.pctsmigration.certificate;

import ch.puzzle.pctsmigration.ExtractionPipeline;
import ch.puzzle.pctsmigration.service.pcts.CertificateTypeService;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CertificateDto;
import org.openapitools.client.model.CertificateInputDto;
import org.openapitools.client.model.CertificateTypeDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

@Component
public class CertificateExtractionPipeline implements ExtractionPipeline<CertificateContextModel, List<CertificateDto>, CertificateInputDto> {
    private final CertificateTypeService certificateTypeService;

    public CertificateExtractionPipeline(CertificateTypeService certificateTypeService) {
        this.certificateTypeService = certificateTypeService;
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
    public String systemPrompt(CertificateContextModel context, String content) {
        return """
            You are a highly precise data extraction assistant. Your task is to process parsed spreadsheet data and extract a LIST of certificate records into a strict JSON array.

            CRITICAL EXTRACTION RULES:
            1. Output Format: Respond ONLY with a valid JSON array of objects matching the requested schema. No conversational text before or after the JSON.
            2. Each data row under the 'Zertifikat' column represents exactly ONE certificate object in the resulting array.
            3. The name of a certificate is always listed in the "Zertifikat" column
            4. Points are calculated in the input as follows: There are columns for the possible point values (0, 0.5, 1, 1.5, 2). The actual point value for a row is indicated by a '1' in the corresponding column. For example, if a row contains a '1' in the '0.5' column, this means that this certificate is worth 0.5 points.
            CONTEXT:
            
            """;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Class<List<CertificateDto>> entityClass() {
        return null;
    }

    @Override
    public Function<List<CertificateDto>, CertificateInputDto> mapToDto() {
        return null;
    }
}
