package ch.puzzle.pcts.dto.calculation;

import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationInputDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationInputDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

//Todo find out what else this DTO needs to have. Does it need ...InputDto or does it need ...Dtos find out what witch dto kind fits better
public record CalculationTemplateDto(
        @Schema(description = "Certificates associated with the calculation.") List<CertificateCalculationInputDto> certificateCalculations,

        @Schema(description = "Leadership experiences associated with the calculation.") List<LeadershipExperienceCalculationInputDto> leadershipExperienceCalculations,

        @Schema(description = "Degrees associated with the calculation.") List<DegreeCalculationInputDto> degreeCalculations,

        @Schema(description = "Experiences associated with the calculation.") List<ExperienceCalculationInputDto> experienceCalculations) {
}
