package ch.puzzle.pcts.dto.calculation;

import ch.puzzle.pcts.model.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record RolePointDto(@Schema(description = "The role associated with the points", example = "1") Role role,

        @Schema(description = "The points form all the calculations of the role", example = "ACTIVE") BigDecimal points) {
}
