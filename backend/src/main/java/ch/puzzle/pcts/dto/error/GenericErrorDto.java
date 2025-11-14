package ch.puzzle.pcts.dto.error;

import ch.puzzle.pcts.model.error.ErrorKey;
import java.util.Map;

public record GenericErrorDto(String key, Map<String, String> values) {
    public GenericErrorDto(ErrorKey key) {
        this(key.name(), Map.of());
    }
}
