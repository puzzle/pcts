package ch.puzzle.pcts.dto.calculation.experiencecalculation;

import ch.puzzle.pcts.model.calculation.Relevancy;
import io.swagger.v3.oas.annotations.media.Schema;

public record ExperienceCalculationInputDto(@Schema(example = "1") Long id, @Schema(example = "1") Long experienceId,
        @Schema(example = "highly") Relevancy relevancy) {
}
