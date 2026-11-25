package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.model.Cell;
import ch.puzzle.pctsmigration.ods.model.Row;
import ch.puzzle.pctsmigration.ods.model.Sheet;
import java.util.List;
import java.util.stream.IntStream;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
class OdsSheetExtractor {

    private static final int MAX_SHEETS = 10;
    private static final int MAX_ROWS = 500;
    private static final int MAX_COLS = 50;
    private static final int CALCULATION_COLUMN_INDEX = 5;

    List<Sheet> extractSheets(OdfSpreadsheetDocument doc, OdsParseConfig config) {
        List<Sheet> sheets = doc
                .getSpreadsheetTables()
                .stream()
                .filter(table -> config.tableNameConvention().test(table.getTableName()))
                .limit(MAX_SHEETS)
                .map(table -> extractSheet(table, config))
                .toList();

        if (sheets.isEmpty()) {
            throw new MigrationException(new Error(HttpStatus.BAD_REQUEST, "No valid sheets found"));
        }

        return sheets;
    }

    private Sheet extractSheet(OdfTable table, OdsParseConfig config) {
        SheetRowCollector collector = new SheetRowCollector(config.startMarker());
        int rowCount = Math.min(table.getRowCount(), MAX_ROWS);
        int colCount = Math.min(table.getColumnCount(), MAX_COLS);

        for (int r = 0; r < rowCount && !collector.isDone(); r++) {
            OdfTableRow odfRow = table.getRowByIndex(r);
            if (odfRow != null) {
                collector.accept(extractRow(odfRow, colCount, config.shouldCutOffCalcColumn()));
            }
        }

        return new Sheet(table.getTableName(), collector.collectedRows());
    }

    private Row extractRow(OdfTableRow odfRow, int colCount, boolean shouldCutOffCalcColumn) {
        List<Cell> cells = IntStream
                .range(0, colCount)
                .mapToObj(colIndex -> extractCell(odfRow, colIndex, shouldCutOffCalcColumn))
                .toList();
        return new Row(cells);
    }

    private Cell extractCell(OdfTableRow odfRow, int colIndex, boolean shouldCutOffCalcColumn) {
        if (shouldCutOffCalcColumn && colIndex == CALCULATION_COLUMN_INDEX) {
            return Cell.EMPTY;
        }
        OdfTableCell odfCell = odfRow.getCellByIndex(colIndex);
        return odfCell == null ? Cell.EMPTY : Cell.of(odfCell.getDisplayText());
    }
}