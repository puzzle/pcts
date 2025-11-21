package ch.puzzle.pcts.dto.error;

import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import java.util.Map;

public record GenericErrorDto(ErrorKey key, Map<FieldKey, String> values) {
    public GenericErrorDto(ErrorKey key) {
        this(key, Map.of());
    }
}
