package ch.puzzle.pcts.dto.support;

import io.swagger.v3.oas.annotations.media.Schema;

public record SupportDto(@Schema(description = "Provides the url the help button should lead to") String url) {

}
