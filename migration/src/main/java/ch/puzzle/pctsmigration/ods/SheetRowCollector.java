package ch.puzzle.pctsmigration.ods;

import java.util.ArrayList;
import java.util.List;

public class SheetRowCollector {

    private static final int MAX_TOLERATED_EMPTY_ROWS = 3;

    private final String requiredStartMarker;
    private final boolean stopsAfterEmptyRows;
    private final List<List<String>> validRows = new ArrayList<>();

    private boolean isRecording;
    private int consecutiveEmptyRows = 0;

    public SheetRowCollector(String startMarker) {
        this.requiredStartMarker = startMarker;

        boolean hasNoMarker = startMarker == null || startMarker.isBlank();
        this.isRecording = hasNoMarker;
        this.stopsAfterEmptyRows = !hasNoMarker;
    }

    public void processRow(List<String> cells) {
        boolean isRowEmpty = cells.stream().allMatch(String::isEmpty);

        if (!isRecording) {
            searchForStartMarker(cells, isRowEmpty);
        } else {
            saveRow(cells, isRowEmpty);
        }
    }

    public boolean isDone() {
        return stopsAfterEmptyRows && consecutiveEmptyRows >= MAX_TOLERATED_EMPTY_ROWS;
    }

    private void searchForStartMarker(List<String> cells, boolean isRowEmpty) {
        if (isRowEmpty) {
            return;
        }

        if (containsStartMarker(cells)) {
            isRecording = true;
            validRows.add(cells);
        }
    }

    private void saveRow(List<String> cells, boolean isRowEmpty) {
        if (isRowEmpty) {
            consecutiveEmptyRows++;
        } else {
            consecutiveEmptyRows = 0;
            validRows.add(cells);
        }
    }

    private boolean containsStartMarker(List<String> cells) {
        return cells.stream().anyMatch(cell -> cell.contains(requiredStartMarker));
    }

    public List<List<String>> getCollectedRows() {
        return validRows;
    }
}