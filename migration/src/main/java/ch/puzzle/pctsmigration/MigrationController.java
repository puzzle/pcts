package ch.puzzle.pctsmigration;

import ch.puzzle.pctsmigration.certificates.CertificateExtractionPipeline;
import ch.puzzle.pctsmigration.extractor.ExtractorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import org.openapitools.client.ApiException;
import org.openapitools.client.model.CertificateInputDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Analyse an ODS certificate sheet and create entries in pcts-api", description = "Upload an .ods file, receive AI-generated CertificateInputDtos, and create them in pcts-api.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Certificates successfully created in pcts-api") })
    public ResponseEntity<List<CertificateInputDto>> certificates(@RequestPart("file") MultipartFile file)
            throws IOException, ApiException {
        List<CertificateInputDto> result = service.extract(file, certificateExtractionPipeline);
        certificateExtractionPipeline.create(result);

        return ResponseEntity.status(201).body(result);
    }
}
