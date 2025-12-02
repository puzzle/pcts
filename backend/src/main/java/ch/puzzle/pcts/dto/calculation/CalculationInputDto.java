package ch.puzzle.pcts.dto.calculation;

import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.role.RoleDto;
import ch.puzzle.pcts.model.calculation.CalculationState;
import io.swagger.v3.oas.annotations.media.Schema;

public record CalculationInputDto(
        @Schema(description = "The member id associated with this calculation.", exampleClasses = MemberDto.class, example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long memberId,

        @Schema(description = "The role id of this calculation.", exampleClasses = RoleDto.class, example = "1", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) Long roleId,

        @Schema(description = "The current state of the calculation.", example = "ACTIVE", requiredMode = Schema.RequiredMode.REQUIRED, nullable = false) CalculationState state) {
}
