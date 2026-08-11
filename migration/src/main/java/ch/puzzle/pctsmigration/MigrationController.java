package ch.puzzle.pctsmigration;

import ch.puzzle.pctsmigration.certificates.CertificateExtractionPipeline;
import ch.puzzle.pctsmigration.extractor.ExtractorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CertificateInputDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI", description = "Interact with the AI service")
public class MigrationController {

    private final ExtractorService service;
    private final CertificateExtractionPipeline certificateExtractionPipeline;

    public MigrationController(ExtractorService service, CertificateExtractionPipeline certificateExtractionPipeline) {
        this.service = service;
        this.certificateExtractionPipeline = certificateExtractionPipeline;
    }

    @PostMapping(value = "/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Analyse an ODS certificate sheet", description = "Upload an .ods file and receive AI-generated CertificateInputDtos")
    public List<CertificateInputDto> certificates(@RequestPart("file") MultipartFile file)
            throws IOException, ApiException {
        List<CertificateInputDto> result = service.extract(file, certificateExtractionPipeline);
        certificateExtractionPipeline.create(result);
        return service.extract(file, certificateExtractionPipeline);
    }
}
