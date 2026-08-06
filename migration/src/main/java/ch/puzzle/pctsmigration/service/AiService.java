package ch.puzzle.pctsmigration.service;

import ch.puzzle.pctsmigration.model.MovieReview;
import ch.puzzle.pctsmigration.model.OdsAnalysisResult;
import jakarta.validation.Validator;
import org.openapitools.client.model.CertificateInputDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class AiService {
    private final ChatClient client;
    private final Validator validator;

    public AiService(ChatClient.Builder builder, Validator validator) {
        this.client = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.validator = validator;
    }

    public MovieReview prompt(String prompt) {
        MovieReview review =  this.client.prompt(prompt).user(u -> {
            u.text("Give me a review of the movie {movie}");
            u.param("movie", prompt);
        }).call().entity(MovieReview.class);

        var violations = validator.validate(review);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("LLM generated invalid rating bounds: " + violations);
        }

        return review;
    }

    public OdsAnalysisResult analyzeOds(String parsedOdsContent) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        OdsAnalysisResult result = this.client.prompt()
                .system("You are a data analyst. Respond ONLY with a valid JSON object matching the requested schema. No text before or after the JSON.")
                .user(u -> u.text("The current date is: {date}. Analyse this spreadsheet content and extract structured insights:\n\n{content}")
                        .param("date", currentDate)
                        .param("content", parsedOdsContent))
                .call()
                .entity(OdsAnalysisResult.class, ChatClient.EntityParamSpec::validateSchema);

        var violations = validator.validate(result);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("LLM returned invalid OdsAnalysisResult: " + violations);
        }
        return result;
    }

    public List<CertificateInputDto> analyzeOdsCertificates(String parsedOdsContent) {
        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        List<CertificateInputDto> result = this.client.prompt()
                .system("""
                You are a highly precise data extraction assistant. Your task is to process parsed spreadsheet data and extract a LIST of certificate records into a strict JSON array.
                
                CRITICAL EXTRACTION RULES:
                1. Output Format: Respond ONLY with a valid JSON array of objects matching the requested schema. No conversational text before or after the JSON.
                2. Row Processing: Each data row under the 'Zertifikat' column (e.g., 'Egnlisch C1') represents exactly ONE certificate object in the resulting array.
                3. Ignoring Unnecessary Data: Completely IGNORE the numerical scoring grid (columns 0, 0.5, 1, 1.5, 2) as seen in the reference file "image_c7207a.png". 
                4. Handling IDs: Set both 'memberId' and 'certificateTypeId' to exactly 0 (dummy values) for every record. 
                5. Valid Until: The 'validUntil' field MUST always be explicitly set to null.
                6. Date Formatting (completedAt): Extract the date from the 'Datum' column and convert it strictly to ISO 8601 format (YYYY-MM-DD). Pay attention to the input format (e.g., '05/01/20' becomes '2020-01-05').
                7. Comment Logic: 
                   - Put the name of the certificate (e.g., 'Egnlisch C1') into the 'comment' field so it can be identified later.
                   - If there is an actual comment for a specific row, it is located strictly to the most right column. If such a comment exists, append it to the certificate name in the 'comment' field.
                   - IMPORTANT: Global footnotes or remarks at the bottom of the document (e.g., texts starting with "Anmerkung:") are NOT comments for a certificate and MUST be completely ignored.
                """)
                .user(u -> u.text("The current date is: {date}. Extract the certificate records from the following parsed spreadsheet content: {content}")
                        .param("date", currentDate)
                        .param("content", parsedOdsContent))
                .call()
                .entity(new ParameterizedTypeReference<List<CertificateInputDto>>() {}, ChatClient.EntityParamSpec::validateSchema);
        result.forEach(dot -> dot.setValidUntil(null));

//        var violations = validator.validate(result);
//        if (!violations.isEmpty()) {
//            throw new IllegalArgumentException("LLM returned invalid OdsAnalysisResult: " + violations);
//        }
        return result;
    }
}
