package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.ods.model.Row;
import java.util.ArrayList;
import java.util.List;

class SheetRowCollector {

    private static final int MAX_TOLERATED_EMPTY_ROWS = 3;

    private final String startMarker;
    private final boolean stopsAfterEmptyRows;
    private final List<Row> rows = new ArrayList<>();

    private boolean recording;
    private int consecutiveEmptyRows;

    SheetRowCollector(String startMarker) {
        this.startMarker = startMarker;
        boolean hasMarker = startMarker != null && !startMarker.isBlank();
        this.recording = !hasMarker;
        this.stopsAfterEmptyRows = hasMarker;
    }

    void accept(Row row) {
        if (row.isEmpty()) {
            if (recording) {
                consecutiveEmptyRows++;
            }
            return;
        }

        if (!recording && !row.containsText(startMarker)) {
            return;
        }

        recording = true;
        consecutiveEmptyRows = 0;
        rows.add(row);
    }

    boolean isDone() {
        return stopsAfterEmptyRows && consecutiveEmptyRows >= MAX_TOLERATED_EMPTY_ROWS;
    }

    List<Row> collectedRows() {
        return List.copyOf(rows);
    }
}