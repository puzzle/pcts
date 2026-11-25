package ch.puzzle.pctsmigration.ods.model;

import java.util.List;

public record Row(List<Cell> cells) {

    public Row {
        cells = cells == null ? List.of() : List.copyOf(cells);
    }

    public boolean isEmpty() {
        return cells.stream().allMatch(Cell::isEmpty);
    }

    public int columnCount() {
        return cells.size();
    }

    public boolean containsText(String marker) {
        return marker != null && !marker.isEmpty() && cells.stream().anyMatch(cell -> cell.contains(marker));
    }

    public boolean containsAnyText(List<String> markers) {
        return markers != null && !markers.isEmpty() && markers.stream().anyMatch(this::containsText);
    }

    public int lastNonEmptyColumnIndex() {
        for (int i = cells.size() - 1; i >= 0; i--) {
            if (!cells.get(i).isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    public Row truncateTo(int maxColumns) {
        int limit = Math.max(0, maxColumns);
        if (limit >= cells.size()) {
            return this;
        }
        return new Row(cells.subList(0, limit));
    }
}