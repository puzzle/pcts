package ch.puzzle.pctsmigration.ods;

import ch.puzzle.pctsmigration.ods.model.Row;
import ch.puzzle.pctsmigration.ods.model.Sheet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SheetSplitter {

    List<List<Sheet>> split(Map<Sheet, List<String>> sheetsWithSplitMarkers) {
        if (sheetsWithSplitMarkers == null || sheetsWithSplitMarkers.isEmpty()) {
            return List.of();
        }

        if (hasNoSplitMarkers(sheetsWithSplitMarkers)) {
            List<Sheet> trimmed = sheetsWithSplitMarkers
                    .keySet()
                    .stream()
                    .map(Sheet::trimTrailingEmptyColumns)
                    .toList();
            return List.of(trimmed);
        }

        List<List<Sheet>> parts = new ArrayList<>();
        List<Sheet> currentPart = new ArrayList<>();

        for (Map.Entry<Sheet, List<String>> entry : sheetsWithSplitMarkers.entrySet()) {
            Sheet sheet = entry.getKey();
            List<String> splitMarkers = sanitizeMarkers(entry.getValue());
            List<Row> currentRows = new ArrayList<>();

            for (Row row : sheet.rows()) {
                boolean isSplitPoint = !splitMarkers.isEmpty() && row.containsAnyText(splitMarkers);
                boolean hasContent = !currentPart.isEmpty() || !currentRows.isEmpty();

                if (isSplitPoint && hasContent) {
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

    private boolean hasNoSplitMarkers(Map<Sheet, List<String>> sheetsWithSplitMarkers) {
        return sheetsWithSplitMarkers.values().stream().allMatch(markers -> sanitizeMarkers(markers).isEmpty());
    }

    private List<String> sanitizeMarkers(List<String> markers) {
        if (markers == null || markers.isEmpty()) {
            return List.of();
        }
        return markers.stream().filter(marker -> marker != null && !marker.isBlank()).toList();
    }

    private void flushRowsIntoPart(List<Sheet> part, String sheetName, List<Row> rows) {
        if (!rows.isEmpty()) {
            part.add(new Sheet(sheetName, rows).trimTrailingEmptyColumns());
        }
    }
}