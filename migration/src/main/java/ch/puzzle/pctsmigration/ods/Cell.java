package ch.puzzle.pctsmigration.ods;

import org.odftoolkit.odfdom.doc.table.OdfTableCell;

public class Cell {
    private String text;

    public Cell(OdfTableCell cell) {
        this.text = cleanCellText(cell);
    }

    private String cleanCellText(OdfTableCell cell) {
        if (cell == null)
            return "";

        String text = cell.getDisplayText();
        if (text == null)
            return "";

        // Removes non-breaking spaces, zero-width spaces, and other phantom characters.
        text = text.replaceAll("[\\u00A0\\u200B\\u200C\\u200D\\uFEFF]", " ").trim();

        if (text.isBlank() || text.equals("0")) {
            return "";
        }
        return text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
