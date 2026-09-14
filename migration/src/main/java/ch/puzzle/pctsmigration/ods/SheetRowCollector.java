package ch.puzzle.pctsmigration.ods;

import java.util.ArrayList;
import java.util.List;

public class SheetRowCollector {
    private final String startMarker;
    private final boolean shouldCutOff;
    private final List<List<String>> collectedRows = new ArrayList<>();

    private boolean isCollecting;
    private int emptyRowCount = 0;

    public SheetRowCollector(String startMarker) {
        this.startMarker = startMarker;
        this.isCollecting = startMarker == null || startMarker.isBlank();
        this.shouldCutOff = !this.isCollecting;
    }

    public boolean processRowAndCheckIfDone(List<String> cells) {
        boolean isNoInfo = cells.stream().allMatch(String::isEmpty);

        if (!isCollecting) {
            if (!isNoInfo && cells.stream().anyMatch(cell -> cell.contains(startMarker))) {
                isCollecting = true;
                collectedRows.add(cells);
            }
            return false;
        }

        if (isNoInfo) {
            emptyRowCount++;
            return shouldCutOff && emptyRowCount >= 3;
        } else {
            emptyRowCount = 0;
            collectedRows.add(cells);
            return false;
        }
    }

    public List<List<String>> getCollectedRows() {
        return collectedRows;
    }
}