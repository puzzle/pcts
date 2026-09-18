package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.ods.model.Row;
import ch.puzzle.pctsmigration.ods.model.Sheet;

public class SheetRowCollector {

    private static final int MAX_TOLERATED_EMPTY_ROWS = 3;

    private final String requiredStartMarker;
    private final boolean stopsAfterEmptyRows;
    private final Sheet sheet;

    private boolean isRecording;
    private int consecutiveEmptyRows = 0;

    public SheetRowCollector(String startMarker) {
        this.sheet = new Sheet();
        this.requiredStartMarker = startMarker;

        boolean hasNoMarker = startMarker == null || startMarker.isBlank();
        this.isRecording = hasNoMarker;
        this.stopsAfterEmptyRows = !hasNoMarker;
    }

    public void processRow(Row row) {
        boolean isRowEmpty = row.getCells().stream().allMatch(cell -> cell.getText().isEmpty());

        if (!isRecording) {
            searchForStartMarker(row, isRowEmpty);
        } else {
            saveRow(row, isRowEmpty);
        }
    }

    public boolean isDone() {
        return stopsAfterEmptyRows && consecutiveEmptyRows >= MAX_TOLERATED_EMPTY_ROWS;
    }

    private void searchForStartMarker(Row row, boolean isRowEmpty) {
        if (isRowEmpty) {
            return;
        }

        if (row.containsStartMarker(requiredStartMarker)) {
            isRecording = true;
            sheet.addRow(row);
        }
    }

    private void saveRow(Row row, boolean isRowEmpty) {
        if (isRowEmpty) {
            consecutiveEmptyRows++;
        } else {
            consecutiveEmptyRows = 0;
            sheet.addRow(row);
        }
    }

    public Sheet getSheet() {
        return sheet;
    }
}