package ch.puzzle.pctsmigration.ods.model;

import org.odftoolkit.odfdom.doc.table.OdfTableCell;

public class Cell {
    private String text;

    public Cell(OdfTableCell cell) {
        this.text = cleanCellText(cell);
    }

    private String cleanCellText(OdfTableCell cell) {
        if (cell == null) {
            return "";
        }

        String displayText = cell.getDisplayText();
        if (displayText == null) {
            return "";
        }

        // Removes non-breaking spaces, zero-width spaces, and other phantom characters.
        displayText = displayText.replaceAll("[\\u00A0\\u200B\\u200C\\u200D\\uFEFF]", " ").trim();
        if (displayText.isBlank() || displayText.equals("0")) {
            return "";
        }

        return displayText;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
