package ch.puzzle.pcts.dto.calculation;

import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationInputDto;
import ch.puzzle.pcts.model.calculation.CalculationState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record CalculationInputDto(

        @Schema(description = "The member id associated with this calculation.", example = "1") Long memberId,

        @Schema(description = "The current state of the calculation.", example = "ACTIVE") CalculationState state,

        @Schema(description = "The role id for this calculation.", example = "1") Long roleId,

        @Schema(description = "Certificates associated with the calculation.") List<CertificateCalculationInputDto> certificateCalculations,

        @Schema(description = "Leadership experiences associated with the calculation.") List<LeadershipExperienceCalculationInputDto> leadershipExperienceCalculations,

        @Schema(description = "Degrees associated with the calculation.") List<DegreeCalculationInputDto> degreeCalculations,

        @Schema(description = "Experiences associated with the calculation.") List<ExperienceCalculationInputDto> experienceCalculations) {
}