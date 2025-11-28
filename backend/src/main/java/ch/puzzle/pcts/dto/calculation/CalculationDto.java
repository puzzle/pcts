package ch.puzzle.pcts.dto.calculation;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.calculation.CalculationState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record CalculationDto(
        @Schema(description = "The unique identifier of the member role assignment.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_ONLY, nullable = false) Long id,

        @Schema(description = "The member associated with this role assignment.", exampleClasses = MemberDto.class, example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) MemberDto member,

        @Schema(description = "The role assigned to the member.", exampleClasses = RoleDto.class, example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) RoleDto role,

        @Schema(description = "The current calculation state for this role assignment.", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) CalculationState state,

        @Schema(description = "The publication date of the role assignment.", example = "2025-03-10", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) LocalDate publicationDate,

        @Schema(description = "The user who publicized the role assignment.", example = "admin_user", requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true) String publicizedBy) {
}
