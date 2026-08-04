package ch.puzzle.pctsmigrationpoc.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

public record MovieReview(
        @JsonPropertyDescription("The exact title of the movie")
        String title,

        @JsonPropertyDescription("A rating from 0 to 5, where 5 is excellent and 0 is terrible")
        @Min(0)
        @Max(5)
        int rating,

        @JsonPropertyDescription("List of 2-3 positive aspects of the movie")
        List<String> pros,

        @JsonPropertyDescription("List of 2-3 negative aspects or criticisms")
        List<String> cons
) {}
