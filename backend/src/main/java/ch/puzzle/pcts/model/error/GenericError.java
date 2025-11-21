package ch.puzzle.pcts.model.error;

import java.util.Map;

public class GenericError {
    private final ErrorKey key;
    private final Map<FieldKey, String> values;

    public GenericError(ErrorKey key, Map<FieldKey, String> values) {
        this.key = key;
        this.values = values;
    }

    public ErrorKey getKey() {
        return key;
    }

    public Map<FieldKey, String> getValues() {
        return values;
    }
}
