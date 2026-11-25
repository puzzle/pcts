package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.ods.model.Row;
import ch.puzzle.pctsmigration.ods.model.Sheet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
class SheetSplitter {

    List<List<Sheet>> split(Map<Sheet, List<String>> sheetsWithSplitMarkers) {
        if (sheetsWithSplitMarkers == null || sheetsWithSplitMarkers.isEmpty()) {
            return List.of();
        }

        List<List<Sheet>> parts = new ArrayList<>();
        List<Sheet> currentPart = new ArrayList<>();

        for (Map.Entry<Sheet, List<String>> entry : sheetsWithSplitMarkers.entrySet()) {
            Sheet sheet = entry.getKey();
            List<String> splitMarkers = sanitizeMarkers(entry.getValue());

            if (sheet.isEmpty()) {
                currentPart.add(sheet);
                continue;
            }

            List<Row> currentRows = new ArrayList<>();
            for (Row row : sheet.rows()) {
                if (isSplitTrigger(row, splitMarkers) && hasCollectedContent(currentPart, currentRows)) {
                    flushRows(currentPart, sheet.name(), currentRows);
                    parts.add(List.copyOf(currentPart));
                    currentPart = new ArrayList<>();
                    currentRows = new ArrayList<>();
                }
                currentRows.add(row);
            }

            flushRows(currentPart, sheet.name(), currentRows);
        }

        if (!currentPart.isEmpty()) {
            parts.add(List.copyOf(currentPart));
        }

        return List.copyOf(parts);
    }

    private boolean isSplitTrigger(Row row, List<String> splitMarkers) {
        return !splitMarkers.isEmpty() && row.containsAnyText(splitMarkers);
    }

    private boolean hasCollectedContent(List<Sheet> currentPart, List<Row> currentRows) {
        return !currentPart.isEmpty() || !currentRows.isEmpty();
    }

    private List<String> sanitizeMarkers(List<String> markers) {
        if (markers == null || markers.isEmpty()) {
            return List.of();
        }
        return markers.stream().filter(marker -> marker != null && !marker.isBlank()).toList();
    }

    private void flushRows(List<Sheet> part, String sheetName, List<Row> rows) {
        if (!rows.isEmpty()) {
            part.add(new Sheet(sheetName, rows));
        }
    }
}