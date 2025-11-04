package ch.puzzle.pcts.dto.error;

import java.util.Map;

public record GenericErrorDto(String key, Map<String, String> values) {
}
