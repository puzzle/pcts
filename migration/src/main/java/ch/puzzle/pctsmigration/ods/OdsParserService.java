package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import ch.puzzle.pctsmigration.ods.model.Cell;
import ch.puzzle.pctsmigration.ods.model.Row;
import ch.puzzle.pctsmigration.ods.model.Sheet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OdsParserService {

    private static final int MAX_SHEETS = 10;
    private static final int MAX_ROWS = 500;
    private static final int MAX_COLS = 50;
    private static final int CALCULATION_COLUMN_INDEX = 5;

    public String parseToPromptText(MultipartFile file, OdsParseConfig config) {
        if (file.isEmpty()) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), "Uploaded file is empty"));
        }

        try (OdfSpreadsheetDocument doc = OdfSpreadsheetDocument.loadDocument(file.getInputStream())) {

            List<Sheet> result = extractData(doc,
                                             config.tableNameConvention(),
                                             config.startMarker(),
                                             config.shouldCutOfCalcRow());
            return generateMarkdown(result);
        } catch (Exception e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "Failed to parse ODS file: " + e.getMessage()));
        }
    }

    private List<Sheet> extractData(OdfSpreadsheetDocument doc, Predicate<String> tableNameConvention,
                                    String startMarker, boolean shouldCutOfCalcRow)
            throws MigrationException {
        List<Sheet> sheets = doc
                .getSpreadsheetTables()
                .stream()
                .filter(table -> tableNameConvention.test(table.getTableName()))
                .limit(MAX_SHEETS)
                .map(table -> extractSheet(table, startMarker, shouldCutOfCalcRow))
                .toList();

        if (sheets.isEmpty()) {
            throw new MigrationException(new Error(HttpStatus.BAD_REQUEST, "No valid sheets found"));
        }
        return sheets;
    }

    private Sheet extractSheet(OdfTable table, String startMarker, boolean shouldCutOfCalcRow) {
        SheetRowCollector collector = new SheetRowCollector(startMarker);

        int rowCount = Math.min(table.getRowCount(), MAX_ROWS);
        int colCount = Math.min(table.getColumnCount(), MAX_COLS);

        for (int r = 0; r < rowCount; r++) {
            OdfTableRow odfTableRow = table.getRowByIndex(r);
            Row row = extractRow(odfTableRow, colCount);

            if (shouldCutOfCalcRow) {
                cutOfCalculationRow(row);
            }

            collector.processRow(row);

            if (collector.isDone()) {
                break;
            }
        }

        List<Row> optimizedRows = optimizeColumns(collector.getSheet().getRows());
        return new Sheet(table.getTableName(), optimizedRows);
    }

    // We're overwriting the entire 5th column here because some ODS files have a
    // calculation column there that we don't want to extract.
    private void cutOfCalculationRow(Row row) {
        if (!Objects.equals(row.getCells().get(CALCULATION_COLUMN_INDEX).getText(), "")) {
            row.updateCellTextByIndex(5, "");
        }
    }

    private Row extractRow(OdfTableRow odfTableRow, int colCount) {
        Row row = new Row();

        IntStream.range(0, colCount).mapToObj(odfTableRow::getCellByIndex).map(Cell::new).forEach(row::addCell);

        return row;
    }

    private List<Row> optimizeColumns(List<Row> rows) {
        if (rows.isEmpty())
            return rows;

        // Find the rightmost column that still contains data
        int maxCol = 0;
        for (Row row : rows) {
            for (int c = row.getCells().size() - 1; c >= maxCol; c--) {
                if (!row.getCells().get(c).getText().isEmpty()) {
                    maxCol = Math.max(maxCol, c + 1);
                    break;
                }
            }
        }

        // Trim all rows to this maximum width with data
        List<Row> optimized = new ArrayList<>(rows.size());
        for (Row row : rows) {
            optimized.add(new Row(row.getCells().subList(0, Math.min(row.getCells().size(), maxCol))));
        }
        return optimized;
    }

    private String generateMarkdown(List<Sheet> result) {
        StringBuilder sb = new StringBuilder();
        for (Sheet sheet : result) {
            appendSheetMarkdown(sb, sheet);
        }
        return sb.toString();
    }

    private void appendSheetMarkdown(StringBuilder sb, Sheet sheet) {
        sb.append("## Sheet: ").append(sheet.getName()).append("\n\n");
        List<Row> rows = sheet.getRows();

        if (rows.isEmpty()) {
            sb.append("_(empty sheet)_\n\n");
            return;
        }

        appendMarkdownRow(sb, rows.getFirst());
        appendMarkdownSeparator(sb, rows.getFirst().getCells().size());

        for (int i = 1; i < rows.size(); i++) {
            appendMarkdownRow(sb, rows.get(i));
        }
        sb.append("\n");
    }

    private void appendMarkdownRow(StringBuilder sb, Row row) {
        sb.append("| ");
        for (Cell cell : row.getCells()) {
            sb.append(cell.getText().replace("|", "\\|")).append(" | ");
        }
        sb.append("\n");
    }

    private void appendMarkdownSeparator(StringBuilder sb, int colCount) {
        sb.append("| ");
        sb.repeat("--- | ", Math.max(0, colCount));
        sb.append("\n");
    }
}