package ch.puzzle.pctsmigration.degree;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DegreeAiResultDto(
        @NotNull @NotBlank
        @JsonPropertyDescription("""
                The name of the degree. Locate the 'Ausbildung' column and extract the value exactly one position to its right.
                """) String name,

        @Nullable
        @JsonPropertyDescription("""
                The degree comment, located in the rightmost field of the degree row. If this field is empty or missing, return null.
                """) String comment) {
}
