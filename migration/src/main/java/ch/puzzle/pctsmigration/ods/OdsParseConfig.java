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
 *            sheet
 * @param shouldCutOffCalcColumn
 *            whether to clear the fixed calculation column (index 5) during
 *            extraction
 */
public record OdsParseConfig(Predicate<String> tableNameConvention, String startMarker,
        Map<String, List<String>> splitMarkersBySheet, boolean shouldCutOffCalcColumn) {
    public OdsParseConfig {
        Objects.requireNonNull(tableNameConvention, "tableNameConvention must not be null");
        splitMarkersBySheet = splitMarkersBySheet == null ? Map.of()
                : splitMarkersBySheet
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                        .collect(Collectors
                                .toUnmodifiableMap(Map.Entry::getKey,
                                                   entry -> entry
                                                           .getValue()
                                                           .stream()
                                                           .filter(marker -> marker != null && !marker.isBlank())
                                                           .toList()));
    }

    /**
     * Creates a configuration without any split markers, producing a single
     * Markdown output segment.
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
        return splitMarkersBySheet.getOrDefault(sheetName, List.of());
    }

    public boolean isSplittingEnabled() {
        return splitMarkersBySheet.values().stream().anyMatch(markers -> !markers.isEmpty());
    }
}