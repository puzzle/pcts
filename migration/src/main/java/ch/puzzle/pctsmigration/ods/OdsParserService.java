package ch.puzzle.pctsmigration.ods;

import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.odftoolkit.odfdom.doc.table.OdfTableRow;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OdsParserService {

    private static final int MAX_SHEETS = 10;
    private static final int MAX_ROWS = 500;
    private static final int MAX_COLS = 50;

    public OdsParseResult parse(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty");
        }
        try {
            OdfSpreadsheetDocument doc = OdfSpreadsheetDocument.loadDocument(file.getInputStream());
            List<OdfTable> tables = doc.getTableList();
            List<OdsParseResult.Sheet> sheets = new ArrayList<>();

            int sheetCount = Math.min(tables.size(), MAX_SHEETS);
            for (int s = 0; s < sheetCount; s++) {
                OdfTable table = tables.get(s);
                int rowCount = Math.min(table.getRowCount(), MAX_ROWS);
                int colCount = Math.min(table.getColumnCount(), MAX_COLS);
                List<List<String>> rows = new ArrayList<>();

                for (int r = 0; r < rowCount; r++) {
                    OdfTableRow row = table.getRowByIndex(r);
                    List<String> cells = new ArrayList<>();
                    for (int c = 0; c < colCount; c++) {
                        OdfTableCell cell = row.getCellByIndex(c);
                        cells.add(cell.getDisplayText().trim());
                    }
                    while (!cells.isEmpty() && cells.getLast().isEmpty()) {
                        cells.removeLast();
                    }
                    if (!cells.isEmpty()) {
                        rows.add(cells);
                    }
                }
                sheets.add(new OdsParseResult.Sheet(table.getTableName(), rows));
            }
            return new OdsParseResult(sheets);
        } catch (Exception e) {
            throw new IOException("Failed to parse ODS file: " + e.getMessage(), e);
        }
    }

    public String toPromptText(OdsParseResult result) {
        StringBuilder sb = new StringBuilder();
        for (OdsParseResult.Sheet sheet : result.sheets()) {
            sb.append("## Sheet: ").append(sheet.name()).append("\n\n");
            List<List<String>> rows = sheet.rows();
            if (rows.isEmpty()) {
                sb.append("_(empty sheet)_\n\n");
                continue;
            }
            appendMarkdownRow(sb, rows.getFirst());
            appendMarkdownSeparator(sb, rows.getFirst().size());
            for (int i = 1; i < rows.size(); i++) {
                appendMarkdownRow(sb, rows.get(i));
            }
            sb.append("\n");
        }
        return sb.toString();
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
