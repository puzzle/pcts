package ch.puzzle.pcts.dto.calculation;

import ch.puzzle.pcts.dto.calculation.calculationleadershipexperience.LeadershipExperienceCalculationDto;
import ch.puzzle.pcts.dto.calculation.certificatecalculation.CertificateCalculationDto;
import ch.puzzle.pcts.dto.calculation.degreecalculation.DegreeCalculationDto;
import ch.puzzle.pcts.dto.calculation.experiencecalculation.ExperienceCalculationDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.calculation.CalculationState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CalculationDto(
        @Schema(description = "The unique identifier of this calculation.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY) Long id,

        @Schema(description = "The member associated with this calculation.", requiredMode = Schema.RequiredMode.REQUIRED) MemberDto member,

        @Schema(description = "The role assigned to this calculation.", requiredMode = Schema.RequiredMode.REQUIRED) RoleDto role,

        @Schema(description = "The current state of the calculation.", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED) CalculationState state,

        @Schema(description = "The publication date of the calculation.", example = "2025-03-10") LocalDate publicationDate,

        @Schema(description = "The user who publicized the calculation.", example = "admin_user") String publicizedBy,

        @Schema(description = "Total points of the calculation.", example = "0.1", requiredMode = Schema.RequiredMode.REQUIRED) BigDecimal points,

        @Schema(description = "Certificates used in the calculation.") List<CertificateCalculationDto> certificates,

        @Schema(description = "Leadership experiences used in the calculation.") List<LeadershipExperienceCalculationDto> leadershipExperiences,

        @Schema(description = "Degrees used in the calculation.") List<DegreeCalculationDto> degrees,

        @Schema(description = "Experiences used in the calculation.") List<ExperienceCalculationDto> experiences) {
}
