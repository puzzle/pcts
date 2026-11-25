package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.ods.model.Cell;
import ch.puzzle.pctsmigration.ods.model.Row;
import ch.puzzle.pctsmigration.ods.model.Sheet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
class MarkdownSheetFormatter {

    String format(List<Sheet> sheets) {
        StringBuilder sb = new StringBuilder();
        for (Sheet sheet : sheets) {
            appendSheet(sb, sheet);
        }
        return sb.toString();
    }

    private void appendSheet(StringBuilder sb, Sheet sheet) {
        sb.append("## Sheet: ").append(sheet.name()).append("\n\n");

        if (sheet.isEmpty()) {
            sb.append("_(empty sheet)_\n\n");
            return;
        }

        List<Row> rows = sheet.rows();
        appendRow(sb, rows.getFirst());
        appendSeparator(sb, rows.getFirst().columnCount());

        for (int i = 1; i < rows.size(); i++) {
            appendRow(sb, rows.get(i));
        }
        sb.append("\n");
    }

    private void appendRow(StringBuilder sb, Row row) {
        String formattedCells = row
                .cells()
                .stream()
                .map(Cell::text)
                .map(text -> text.replace("|", "\\|"))
                .collect(Collectors.joining(" | ", "| ", " |\n"));
        sb.append(formattedCells);
    }

    private void appendSeparator(StringBuilder sb, int colCount) {
        sb.append("|");
        sb.repeat(" --- |", Math.max(0, colCount));
        sb.append("\n");
    }
}