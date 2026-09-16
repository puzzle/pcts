package ch.puzzle.pctsmigration.ods.model;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Cell> cells;

    public Row(List<Cell> cells) {
        this.cells = cells;
    }

    public Row() {
        this.cells = new ArrayList<>();
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public void updateCellTextByIndex(int index, String text) {
        cells.get(index).setText(text);
    }

    public boolean containsStartMarker(String requiredStartMarker) {
        return cells.stream().anyMatch(cell -> cell.getText().contains(requiredStartMarker));
    }
}
