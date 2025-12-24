package ch.puzzle.pcts.dto.calculation;

import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationInputDto;
import ch.puzzle.pcts.model.calculation.CalculationState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record CalculationInputDto(

        @Schema(description = "The member id associated with this calculation.", example = "1") Long memberId,

        @Schema(description = "The current state of the calculation.", example = "ACTIVE") CalculationState state,

        @Schema(description = "The role id for this calculation.", example = "1") Long roleId,

        @Schema(description = "Certificates associated with the calculation.", example = "[1, 2, 3]") List<Long> certificates,

        @Schema(description = "Leadership experiences associated with the calculation.", example = "[5, 6]") List<Long> leadershipExperiences,

        @Schema(description = "Degrees associated with the calculation.") List<DegreeCalculationInputDto> degrees,

        @Schema(description = "Experiences associated with the calculation.") List<ExperienceCalculationInputDto> experiences) {
}