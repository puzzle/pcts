package ch.puzzle.pctsmigration.ods;

import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;

public class OdsDimensionsHelper {
    private static final int MAX_ROWS = 500;
    private static final int MAX_COLS = 50;

    public static int[] getDimensions(OdfTable table) {
        int totalRows = Math.min(table.getRowCount(), MAX_ROWS);
        int totalCols = Math.min(table.getColumnCount(), MAX_COLS);

        int realRowCount = 0;
        int realColCount = 0;

        for (int r = totalRows - 1; r >= 0; r--) {
            if (!isRowEmpty(table, r, totalCols)) {
                realRowCount = r + 1;
                break;
            }
        }

        for (int c = totalCols - 1; c >= 0; c--) {
            if (!isColumnEmpty(table, c, realRowCount)) {
                realColCount = c + 1;
                break;
            }
        }

        return new int[]{ realRowCount, realColCount };
    }

    private static boolean isRowEmpty(OdfTable table, int rowIndex, int colCount) {
        for (int c = 0; c < colCount; c++) {
            if (hasMeaningfulContent(table.getCellByPosition(c, rowIndex))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isColumnEmpty(OdfTable table, int colIndex, int rowCount) {
        for (int r = 0; r < rowCount; r++) {
            if (hasMeaningfulContent(table.getCellByPosition(colIndex, r))) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasMeaningfulContent(OdfTableCell cell) {
        if (cell == null)
            return false;

        String content = cell.getStringValue();

        return content != null && !content.trim().isEmpty();
    }
}