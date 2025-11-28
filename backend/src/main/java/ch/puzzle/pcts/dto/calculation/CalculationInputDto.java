package ch.puzzle.pcts.dto.calculation;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.calculation.CalculationState;
import io.swagger.v3.oas.annotations.media.Schema;

public record CalculationInputDto(
        @Schema(description = "The member id associated with this role assignment.", exampleClasses = MemberDto.class, example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long memberId,

        @Schema(description = "The role id assigned to the member.", exampleClasses = RoleDto.class, example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long roleId,

        @Schema(description = "The current calculation state for this role assignment.", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) CalculationState state) {
}
