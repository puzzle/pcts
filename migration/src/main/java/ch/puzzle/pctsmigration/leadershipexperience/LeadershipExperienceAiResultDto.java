package ch.puzzle.pctsmigration.leadershipexperience;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LeadershipExperienceAiResultDto(

        @NotNull @NotBlank
        @JsonPropertyDescription("""
                The name of the leadership experience. Locate the 'Führungserfahrung' column and extract the value exactly one position to its right.
                Ignore subsequent fields, as they are categories.
                """) String name,

        @Nullable
        @JsonPropertyDescription("""
                The certificate comment, located in the rightmost field of the leadership experience row. If this field is empty or missing, return null.
                """) String comment) {
}