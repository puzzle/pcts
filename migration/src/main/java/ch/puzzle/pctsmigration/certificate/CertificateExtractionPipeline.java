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
        return "";
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
