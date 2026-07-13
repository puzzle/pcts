package ch.puzzle.pcts.dto.appconfiguration;

import io.swagger.v3.oas.annotations.media.Schema;

public record AppConfigurationDto(
        @Schema(description = "Provides the URL the help button should lead to") String helpUrl) {

}
