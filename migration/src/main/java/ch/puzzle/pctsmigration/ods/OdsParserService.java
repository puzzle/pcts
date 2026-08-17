package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.exception.Error;
import ch.puzzle.pctsmigration.exception.MigrationException;
import java.util.ArrayList;
import java.util.List;
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

    public String parseToPromptText(MultipartFile file) {
        validateFile(file);

        try {
            OdfSpreadsheetDocument doc = OdfSpreadsheetDocument.loadDocument(file.getInputStream());
            OdsParseResult result = extractData(doc);
            return generateMarkdown(result);

        } catch (Exception e) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400),
                                                   "Failed to parse ODS file: " + e.getMessage()));
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new MigrationException(new Error(HttpStatusCode.valueOf(400), "Uploaded file is empty"));
        }
    }

    private OdsParseResult extractData(OdfSpreadsheetDocument doc) throws Exception {
        List<OdsParseResult.Sheet> sheets = doc.getTableList().stream().filter(table -> {
            String name = table.getTableName();
            return name.equals(name.toLowerCase());
        }).limit(MAX_SHEETS).map(this::extractSheet).toList();

        if (sheets.isEmpty()) {
            throw new Exception("No valid sheets found");
        }
        return new OdsParseResult(sheets);
    }

    private OdsParseResult.Sheet extractSheet(OdfTable table) {
        int rowCount = Math.min(table.getRowCount(), MAX_ROWS);
        int colCount = Math.min(table.getColumnCount(), MAX_COLS);
        List<List<String>> rows = new ArrayList<>();

        for (int r = 0; r < rowCount; r++) {
            List<String> cells = extractRow(table.getRowByIndex(r), colCount);
            if (!cells.isEmpty()) {
                rows.add(cells);
            }
        }
        return new OdsParseResult.Sheet(table.getTableName(), rows);
    }

    private List<String> extractRow(OdfTableRow row, int colCount) {
        List<String> cells = new ArrayList<>();
        for (int c = 0; c < colCount; c++) {
            OdfTableCell cell = row.getCellByIndex(c);
            cells.add(cell.getDisplayText().trim());
        }

        while (!cells.isEmpty() && cells.getLast().isEmpty()) {
            cells.removeLast();
        }
        return cells;
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