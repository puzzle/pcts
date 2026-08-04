package ch.puzzle.pctsmigrationpoc.service;

import ch.puzzle.pctsmigrationpoc.model.MovieReview;
import ch.puzzle.pctsmigrationpoc.model.OdsAnalysisResult;
import jakarta.validation.Validator;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


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
}
