package ch.puzzle.pctsmigration.extractor;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.OdsParserService;
import jakarta.validation.Validator;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
        String parsedToMarkdown = this.odsParserService.parseToPromptText(file);
        C context = pipeline.fetchContext();
        R result = this.aiService.extract(parsedToMarkdown, pipeline.systemPrompt(context), pipeline.entityClass());
        jakartaValidation(result);
        pipeline.additionalValidations(result);
        String filename = getFileName(file);
        return pipeline.mapToDto(filename, result);
    }

    private <R> void jakartaValidation(R result) {
        var violations = validator.validate(result);
        if (!violations.isEmpty()) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), violations.toString()));
        }
    }

    private String getFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (!StringUtils.hasText(fileName)) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), "File name is empty"));
        }

        return fileName;
    }
}