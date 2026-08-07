package ch.puzzle.pctsmigration;

import ch.puzzle.pctsmigration.service.AiService;
import ch.puzzle.pctsmigration.service.OdsParserService;
import org.openapitools.client.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Validator;
import java.io.IOException;
import java.util.Objects;

@Service
public class ExtractorService {
    private final OdsParserService odsParserService;
    private final AiService aiService;
    private final Validator validator;

    public ExtractorService(OdsParserService odsParserService, AiService aiService, Validator validator) {
        this.odsParserService = odsParserService;
        this.aiService = aiService;
        this.validator = validator;
    }

    public <C, R, D> void extract(MultipartFile file, ExtractionPipeline<C, R, D> pipeline) throws IOException, ApiException {
        System.out.println(pipeline.systemPrompt(pipeline.fetchContext()));

        String parsedToMarkdown = this.odsParserService.toPromptText(this.odsParserService.parse(file));
        String abbreviation = Objects.requireNonNull(file.getOriginalFilename()).split("_")[0];
        R result = this.aiService.extractCertificateData(parsedToMarkdown, pipeline.systemPrompt(pipeline.fetchContext()));

        if (pipeline.validate()) {
            var violations = validator.validate(result);
            if (!violations.isEmpty()) {
                throw new IllegalArgumentException("LLM generated invalid rating bounds: " + violations);
            }
        }
    }
}
