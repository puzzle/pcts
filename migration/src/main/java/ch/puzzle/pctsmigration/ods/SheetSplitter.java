package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.ods.model.Row;
import ch.puzzle.pctsmigration.ods.model.Sheet;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SheetSplitter {

    List<List<Sheet>> split(List<Sheet> sheets, List<String> splitMarkers) {
        if (splitMarkers.isEmpty()) {
            List<Sheet> trimmed = sheets.stream().map(Sheet::trimTrailingEmptyColumns).toList();
            return List.of(trimmed);
        }

        List<List<Sheet>> parts = new ArrayList<>();
        List<Sheet> currentPart = new ArrayList<>();

        for (Sheet sheet : sheets) {
            List<Row> currentRows = new ArrayList<>();

            for (Row row : sheet.rows()) {
                if (row.containsAnyText(splitMarkers) && (!currentPart.isEmpty() || !currentRows.isEmpty())) {
                    flushRowsIntoPart(currentPart, sheet.name(), currentRows);
                    parts.add(List.copyOf(currentPart));
                    currentPart = new ArrayList<>();
                    currentRows = new ArrayList<>();
                }
                currentRows.add(row);
            }

            flushRowsIntoPart(currentPart, sheet.name(), currentRows);
        }

        if (!currentPart.isEmpty()) {
            parts.add(List.copyOf(currentPart));
        }

        return List.copyOf(parts);
    }

    private void flushRowsIntoPart(List<Sheet> part, String sheetName, List<Row> rows) {
        if (!rows.isEmpty()) {
            part.add(new Sheet(sheetName, rows).trimTrailingEmptyColumns());
        }
    }
}