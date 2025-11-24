package ch.puzzle.pcts.dto.error;

import java.util.Map;

public record GenericErrorDto(ErrorKey key, Map<FieldKey, String> values) {
    public GenericErrorDto(ErrorKey key) {
        this(key, Map.of());
    }

    public ErrorKey getKey() {
        return key;
    }

    public Map<FieldKey, String> getValues() {
        return values;
    }
}
