package ch.puzzle.pctsmigration.extractor;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.OdsParserService;
import jakarta.validation.Validator;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public <C, R, D> List<D> extract(MultipartFile file, ExtractionPipeline<C, R, D> pipeline) {
        String parsedToMarkdown = getMarkdownTableFrom(file);
        C context = pipeline.fetchContext();
        R result = this.aiService.extract(parsedToMarkdown, pipeline.systemPrompt(context), pipeline.entityClass());
        jakartaValidation(result);
        pipeline.additionalValidations(result);
        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        return pipeline.mapToDto(filename, result);
    }

    private String getMarkdownTableFrom(MultipartFile file) {
        try {
            return this.odsParserService.toPromptText(this.odsParserService.parse(file));
        } catch (IOException e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), e.getMessage()));
        }
    }

    private <R> void jakartaValidation(R result) {
        var violations = validator.validate(result);
        if (!violations.isEmpty()) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), violations.toString()));
        }
    }
}