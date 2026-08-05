package ch.puzzle.pctsmigration.controller;

import ch.puzzle.pctsmigration.model.MovieReview;
import ch.puzzle.pctsmigration.model.OdsAnalysisResult;
import ch.puzzle.pctsmigration.service.AiService;
import ch.puzzle.pctsmigration.service.CertificateApiService;
import ch.puzzle.pctsmigration.service.OdsParserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.openapitools.client.ApiException;
import org.openapitools.client.api.CertificatesApi;
import org.openapitools.client.model.CertificateDto;
import org.openapitools.client.model.CertificateTypeDto;
import org.openapitools.client.model.DegreeDto;
import org.openapitools.client.model.DegreeTypeDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@Tag(name = "AI", description = "Interact with the AI service")
public class AiController {

    private final AiService aiService;
    private final OdsParserService odsParserService;
    private final CertificateApiService certificateApiService;

    public AiController(AiService aiService, OdsParserService odsParserService, CertificateApiService certificateApiService) {
        this.aiService = aiService;
        this.odsParserService = odsParserService;
        this.certificateApiService = certificateApiService;
    }

    @PostMapping(value = "/prompt", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Send a prompt to the AI", description = "Sends a plain-text prompt to the AI model and returns the response.")
    public MovieReview prompt(@RequestBody String prompt) {
        return aiService.prompt(prompt);
    }

    @PostMapping(value = "/analyze-ods", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Analyse an ODS spreadsheet", description = "Upload an .ods file and receive AI-generated structured insights.")
    public OdsAnalysisResult analyzeOds(@RequestPart("file") MultipartFile file) throws IOException {
        var parsed = odsParserService.parse(file);
        return aiService.analyzeOds(odsParserService.toPromptText(parsed));
    }

    @GetMapping("pctsApi")
    public List<DegreeTypeDto> getFromPctsApi() throws ApiException {
        return certificateApiService.getCertificates();
    }
}
