package ch.puzzle.pctsmigration;

import ch.puzzle.pctsmigration.certificate.CertificateExtractionPipeline;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CertificateInputDto;
import org.openapitools.client.model.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI", description = "Interact with the AI service")
public class AiController {

    private final ExtractorService service;
    private final CertificateExtractionPipeline certificateExtractionPipeline;

    public AiController(ExtractorService service, CertificateExtractionPipeline certificateExtractionPipeline) {
        this.service = service;
        this.certificateExtractionPipeline = certificateExtractionPipeline;
    }

    @PostMapping(value = "/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Analyse an ODS certificate sheet", description = "Upload an .ods file and receive AI-generated CertificateInputDtos")
    public List<CertificateInputDto> certificates(@RequestPart("file") MultipartFile file) throws IOException, ApiException {
        return service.extract(file, certificateExtractionPipeline);
    }
}
