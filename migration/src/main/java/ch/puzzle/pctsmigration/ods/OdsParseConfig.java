package ch.puzzle.pctsmigration.ods;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Configuration defining how an ODS spreadsheet is filtered, extracted, and
 * split into one or more Markdown prompt segments.
 *
 * @param tableNameConvention
 *            predicate selecting which spreadsheet tabs (sheets) to include by
 *            name
 * @param startMarker
 *            optional text marker indicating the first row to record within a
 *            sheet; if null or blank, extraction starts from the first row and
 *            reads until the sheet limit
 * @param splitMarkersBySheet
 *            optional map from sheet name to text markers that start a new
 *            output segment whenever a matching row is encountered in that
 *            sheet; pass null or an empty map to disable splitting
 * @param shouldCutOffCalcColumn
 *            whether to clear the fixed calculation column (index 5) during
 *            extraction
 */
public record OdsParseConfig(Predicate<String> tableNameConvention, String startMarker,
        Map<String, List<String>> splitMarkersBySheet, boolean shouldCutOffCalcColumn) {
    public OdsParseConfig {
        Objects.requireNonNull(tableNameConvention, "tableNameConvention must not be null");
        splitMarkersBySheet = sanitizeSplitMarkers(splitMarkersBySheet);
    }

    /**
     * Creates a configuration with splitting disabled, producing a single Markdown
     * output segment.
     *
     * @param tableNameConvention
     *            predicate selecting which spreadsheet tabs to include by name
     * @param startMarker
     *            optional text marker indicating the first row to record
     * @param shouldCutOffCalcColumn
     *            whether to clear the fixed calculation column (index 5) during
     *            extraction
     */
    public OdsParseConfig(Predicate<String> tableNameConvention, String startMarker, boolean shouldCutOffCalcColumn) {
        this(tableNameConvention, startMarker, Map.of(), shouldCutOffCalcColumn);
    }

    /**
     * Returns the configured split markers for the given sheet name, or an empty
     * list if none are defined.
     *
     * @param sheetName
     *            the name of the sheet
     * @return immutable list of split markers for that sheet
     */
    public List<String> splitMarkersFor(String sheetName) {
        if (sheetName == null) {
            return List.of();
        }
        return splitMarkersBySheet.getOrDefault(sheetName, List.of());
    }

    /**
     * Returns whether splitting is enabled for at least one sheet.
     *
     * @return {@code true} if at least one sheet has non-empty split markers,
     *         {@code false} otherwise
     */
    public boolean isSplittingEnabled() {
        return !splitMarkersBySheet.isEmpty();
    }

    private static Map<String, List<String>> sanitizeSplitMarkers(Map<String, List<String>> rawMap) {
        if (rawMap == null || rawMap.isEmpty()) {
            return Map.of();
        }
        return rawMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() != null && !entry.getKey().isBlank() && entry.getValue() != null)
                .map(entry -> Map
                        .entry(entry.getKey(),
                               entry
                                       .getValue()
                                       .stream()
                                       .filter(marker -> marker != null && !marker.isBlank())
                                       .toList()))
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}