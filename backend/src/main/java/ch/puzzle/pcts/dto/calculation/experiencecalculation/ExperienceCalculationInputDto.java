package ch.puzzle.pcts.dto.calculation.experiencecalculation;

import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;

public record ExperienceCalculationInputDto(@Schema(example = "1") Long experienceId,
        @Schema(example = "HIGHLY") Relevancy relevancy,
        @Schema(example = "It's highly because it is the same job.") String comment) {
}
