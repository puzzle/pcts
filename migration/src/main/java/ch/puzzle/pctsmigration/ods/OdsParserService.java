package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OdsParserService {

    private static final int MAX_SHEETS = 10;
    private static final int MAX_ROWS = 500;
    private static final int MAX_COLS = 50;

    public String parseToPromptText(MultipartFile file, OdsParseConfig config) {
        if (file.isEmpty()) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), "Uploaded file is empty"));
        }

        try (OdfSpreadsheetDocument doc = OdfSpreadsheetDocument.loadDocument(file.getInputStream())) {

            OdsParseResult result = extractData(doc,
                                                config.tableNameConvention(),
                                                config.startMarker(),
                                                config.shouldCutOfCalcRow());
            return generateMarkdown(result);
        } catch (Exception e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "Failed to parse ODS file: " + e.getMessage()));
        }
    }

    private OdsParseResult extractData(OdfSpreadsheetDocument doc, Function<String, Boolean> tableNameConvention,
                                       String startMarker, boolean shouldCutOfCalcRow)
            throws Exception {
        List<OdsParseResult.Sheet> sheets = doc
                .getSpreadsheetTables()
                .stream()
                .filter(table -> tableNameConvention.apply(table.getTableName()))
                .limit(MAX_SHEETS)
                .map(table -> extractSheet(table, startMarker, shouldCutOfCalcRow))
                .toList();

        if (sheets.isEmpty()) {
            throw new Exception("No valid sheets found");
        }
        return new OdsParseResult(sheets);
    }

    private OdsParseResult.Sheet extractSheet(OdfTable table, String startMarker, boolean shouldCutOfCalcRow) {
        SheetRowCollector collector = new SheetRowCollector(startMarker);

        int rowCount = Math.min(table.getRowCount(), MAX_ROWS);
        int colCount = Math.min(table.getColumnCount(), MAX_COLS);

        for (int r = 0; r < rowCount; r++) {
            OdfTableRow row = table.getRowByIndex(r);
            List<String> cells = extractRow(row, colCount);

            if (shouldCutOfCalcRow) {
                cutOfCalculationRow(cells);
            }

            collector.processRow(cells);

            if (collector.isDone()) {
                break;
            }
        }

        List<List<String>> optimizedRows = optimizeColumns(collector.getCollectedRows());
        return new OdsParseResult.Sheet(table.getTableName(), optimizedRows);
    }

    // We're overwriting the entire 5th column here because some ODS files have a
    // calculation column there that we don't want to extract.
    private void cutOfCalculationRow(List<String> cells) {
        if (!Objects.equals(cells.get(5), "")) {
            cells.set(5, "");
        }
    }

    private List<String> extractRow(OdfTableRow row, int colCount) {
        List<String> cells = new ArrayList<>(colCount);
        for (int c = 0; c < colCount; c++) {
            OdfTableCell cell = row.getCellByIndex(c);
            cells.add(cleanCellText(cell));
        }
        return cells;
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

    private List<List<String>> optimizeColumns(List<List<String>> rows) {
        if (rows.isEmpty())
            return rows;

        // Find the rightmost column that still contains data
        int maxCol = 0;
        for (List<String> row : rows) {
            for (int c = row.size() - 1; c >= maxCol; c--) {
                if (!row.get(c).isEmpty()) {
                    maxCol = Math.max(maxCol, c + 1);
                    break;
                }
            }
        }

        // Trim all rows to this maximum width with data
        List<List<String>> optimized = new ArrayList<>(rows.size());
        for (List<String> row : rows) {
            optimized.add(row.subList(0, Math.min(row.size(), maxCol)));
        }
        return optimized;
    }

    private String generateMarkdown(OdsParseResult result) {
        StringBuilder sb = new StringBuilder();
        for (OdsParseResult.Sheet sheet : result.sheets()) {
            appendSheetMarkdown(sb, sheet);
        }
        return sb.toString();
    }

    private void appendSheetMarkdown(StringBuilder sb, OdsParseResult.Sheet sheet) {
        sb.append("## Sheet: ").append(sheet.name()).append("\n\n");
        List<List<String>> rows = sheet.rows();

        if (rows.isEmpty()) {
            sb.append("_(empty sheet)_\n\n");
            return;
        }

        appendMarkdownRow(sb, rows.getFirst());
        appendMarkdownSeparator(sb, rows.getFirst().size());

        for (int i = 1; i < rows.size(); i++) {
            appendMarkdownRow(sb, rows.get(i));
        }
        sb.append("\n");
    }

    private void appendMarkdownRow(StringBuilder sb, List<String> cells) {
        sb.append("| ");
        for (String cell : cells) {
            sb.append(cell.replace("|", "\\|")).append(" | ");
        }
        sb.append("\n");
    }

    private void appendMarkdownSeparator(StringBuilder sb, int colCount) {
        sb.append("| ");
        sb.repeat("--- | ", Math.max(0, colCount));
        sb.append("\n");
    }
}