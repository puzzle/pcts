package ch.puzzle.pcts.model.error;

import java.util.Map;

public class GenericError {
    private final ErrorKey key;
    private final Map<String, String> values;

    public GenericError(ErrorKey key, Map<String, String> values) {
        this.key = key;
        this.values = values;
    }

    public ErrorKey getKey() {
        return key;
    }

    public Map<String, String> getValues() {
        return values;
    }
}
