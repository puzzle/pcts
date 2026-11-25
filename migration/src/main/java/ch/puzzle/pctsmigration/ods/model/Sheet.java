package ch.puzzle.pctsmigration.ods.model;

import java.util.List;

public record Sheet(String name, List<Row> rows) {

    public Sheet {
        name = name == null ? "" : name;
        rows = rows == null ? List.of() : List.copyOf(rows);
    }

    public boolean isEmpty() {
        return rows.isEmpty();
    }

    public Sheet trimTrailingEmptyColumns() {
        if (rows.isEmpty()) {
            return this;
        }

        int maxCol = rows.stream().mapToInt(Row::lastNonEmptyColumnIndex).max().orElse(-1) + 1;

        if (maxCol == 0) {
            return new Sheet(name, List.of());
        }

        List<Row> trimmedRows = rows.stream().map(row -> row.truncateTo(maxCol)).toList();

        return new Sheet(name, trimmedRows);
    }
}