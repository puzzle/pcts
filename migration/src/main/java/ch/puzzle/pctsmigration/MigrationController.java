package ch.puzzle.pctsmigration;

import ch.puzzle.pctsmigration.certificates.CertificateExtractionPipeline;
import ch.puzzle.pctsmigration.certificates.MultipleFileResultDto;
import ch.puzzle.pctsmigration.exception.FileError;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.extractor.ExtractorService;
import ch.puzzle.pctsmigration.leadershipexperience.LeadershipExperienceExtractionPipeline;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.openapitools.client.model.CertificateInputDto;
import org.openapitools.client.model.LeadershipExperienceInputDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/migration")
@Tag(name = "Migration", description = "Endpoints for migrating legacy PCTS sheets into the new PCTS tool. Provides AI-assisted data extraction and automatic record creation.")
public class MigrationController {

    private final ExtractorService service;
    private final CertificateExtractionPipeline certificateExtractionPipeline;
    private final LeadershipExperienceExtractionPipeline leadershipExperienceExtractionPipeline;

    public MigrationController(ExtractorService service, CertificateExtractionPipeline certificateExtractionPipeline, LeadershipExperienceExtractionPipeline leadershipExperienceExtractionPipeline) {
        this.service = service;
        this.certificateExtractionPipeline = certificateExtractionPipeline;
        this.leadershipExperienceExtractionPipeline = leadershipExperienceExtractionPipeline;
    }

    @PostMapping(value = "/certificate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Extract and migrate certificates from an ODS file", description = "Uploads a legacy .ods spreadsheet containing certificate data. The system uses an AI-based extraction pipeline to parse the file, generate the corresponding `CertificateInputDto` objects, and automatically persists them in the upstream pcts-api.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Certificate successfully extracted and created in pcts-api.") })
    public ResponseEntity<List<CertificateInputDto>> certificate(@Parameter(description = "The .ods file containing the certificate data to be migrated. Must be a valid OpenDocument Spreadsheet.", required = true)
    @RequestPart("file") MultipartFile file) {
        List<CertificateInputDto> result = service.extract(file, certificateExtractionPipeline);
        certificateExtractionPipeline.create(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping(value = "/certificates", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Extract and migrate certificates from ODS files", description = "Uploads multiple legacy .ods spreadsheet containing certificate data. The system uses an AI-based extraction pipeline to parse the file, generate the corresponding `CertificateInputDto` objects, and automatically persists them in the upstream pcts-api.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Certificates successfully extracted and created in pcts-api.") })
    public ResponseEntity<MultipleFileResultDto> certificates(@RequestPart("files") List<MultipartFile> files) {
        MultipleFileResultDto result = new MultipleFileResultDto();

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();

            try {
                List<CertificateInputDto> extracted = service.extract(file, certificateExtractionPipeline);
                certificateExtractionPipeline.create(extracted);
                result.addToSuccessfulCertificates(filename, extracted);

            } catch (MigrationException e) {
                result.addToFailedFiles(new FileError(filename, e.getMessage()));
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping(value = "/leadershipexperience", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Extract and migrate leadership experiences from an ODS file", description = "Uploads a legacy .ods spreadsheet containing leadership experience data. The system uses an AI-based extraction pipeline to parse the file, generate the corresponding `LeadershipExperienceInputDto` objects, and automatically persists them in the upstream pcts-api. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leadership Experience successfully extracted and created in pcts-api.") })
    public ResponseEntity<List<LeadershipExperienceInputDto>> leadershipExperience(@Parameter(description = "The .ods file containing the certificate data to be migrated. Must be a valid OpenDocument Spreadsheet.", required = true)
    @RequestPart("file") MultipartFile file) {
        List<LeadershipExperienceInputDto> result = service.extract(file, leadershipExperienceExtractionPipeline);
        leadershipExperienceExtractionPipeline.create(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
