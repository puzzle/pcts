package ch.puzzle.pcts.dto.configuration;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record ConfigurationDto(
        @Schema(description = "All roles that are allowed to access admin functionality in the frontend", example = "org_hr") List<String> adminAuthorities) {
}
