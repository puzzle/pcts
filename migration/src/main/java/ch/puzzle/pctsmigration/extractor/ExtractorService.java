package ch.puzzle.pctsmigration.extractor;

import ch.puzzle.pctsmigration.ods.OdsParserService;
import jakarta.validation.Validator;
import java.io.IOException;
import java.util.List;
import org.openapitools.client.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExtractorService {
    private final OdsParserService odsParserService;
    private final ExtractorAiService aiService;
    private final Validator validator;

    public ExtractorService(OdsParserService odsParserService, ExtractorAiService aiService, Validator validator) {
        this.odsParserService = odsParserService;
        this.aiService = aiService;
        this.validator = validator;
    }

    public <C, R, D> List<D> extract(MultipartFile file, ExtractionPipeline<C, R, D> pipeline)
            throws IOException, ApiException {
        String parsedToMarkdown = this.odsParserService.toPromptText(this.odsParserService.parse(file));

        R result = this.aiService
                .aiExtractFrom(parsedToMarkdown,
                                        pipeline.systemPrompt(pipeline.fetchContext()),
                                        pipeline.entityClass());

        if (pipeline.validate()) {
            var violations = validator.validate(result);
            if (!violations.isEmpty()) {
                throw new IllegalArgumentException("LLM generated invalid data: " + violations);
            }
        }

        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        return pipeline.mapToDto(filename, result);
    }
}