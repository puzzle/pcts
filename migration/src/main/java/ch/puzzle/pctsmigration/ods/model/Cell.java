package ch.puzzle.pctsmigration.ods.model;

import java.util.regex.Pattern;

public record Cell(String text) {

    public static final Cell EMPTY = new Cell("");

    private static final Pattern PHANTOM_AND_NEWLINE_PATTERN = Pattern
            .compile("[\\u00A0\\u200B\\u200C\\u200D\\uFEFF\\r\\n]+");

    public Cell {
        text = text == null ? "" : text;
    }

    public static Cell of(String rawText) {
        if (rawText == null) {
            return EMPTY;
        }

        String cleaned = PHANTOM_AND_NEWLINE_PATTERN.matcher(rawText).replaceAll(" ").trim();

        if (cleaned.isBlank() || cleaned.equals("0")) {
            return EMPTY;
        }

        return new Cell(cleaned);
    }

    public boolean isEmpty() {
        return text.isEmpty();
    }

    public boolean contains(String marker) {
        return text.contains(marker);
    }
}