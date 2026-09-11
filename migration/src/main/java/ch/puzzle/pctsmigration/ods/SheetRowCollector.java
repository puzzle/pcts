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
        boolean isNoInfo = isRowWithoutInformation(cells);

        if (!isCollecting) {
            checkStartCondition(cells, isNoInfo);
            return false;
        }

        return handleCollectedRow(cells, isNoInfo);
    }

    private void checkStartCondition(List<String> cells, boolean isNoInfo) {
        if (!isNoInfo && containsMarker(cells, startMarker)) {
            isCollecting = true;
            collectedRows.add(cells);
        }
    }

    private boolean handleCollectedRow(List<String> cells, boolean isNoInfo) {
        if (isNoInfo) {
            return shouldCutOff && ++emptyRowCount >= 3;
        }
        emptyRowCount = 0;
        collectedRows.add(cells);
        return false;
    }

    public List<List<String>> getCollectedRows() {
        return collectedRows;
    }

    private boolean isRowWithoutInformation(List<String> cells) {
        return cells.stream().map(String::trim).allMatch(cell -> cell.isEmpty() || cell.equals("0"));
    }

    private boolean containsMarker(List<String> cells, String marker) {
        return cells.stream().anyMatch(cell -> cell.contains(marker));
    }
}
