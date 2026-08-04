package ch.puzzle.pctsmigrationpoc.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OdsAnalysisResult(
        @JsonPropertyDescription("A one-sentence summary of the spreadsheet's purpose")
        @NotBlank
        String summary,

        @JsonPropertyDescription("List of key data points or findings extracted from the spreadsheet")
        @NotNull
        List<String> keyFindings,

        @JsonPropertyDescription("Any data quality issues, missing values or inconsistencies found")
        @NotNull
        List<String> dataQualityIssues,

        @JsonPropertyDescription("Suggested next steps or actions based on the data")
        @NotNull
        List<String> recommendations
) {}
