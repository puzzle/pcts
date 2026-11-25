package ch.puzzle.pctsmigration.ods;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

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
 * @param splitMarkers
 *            optional list of text markers that start a new output segment
 *            whenever a matching row is encountered; blank or null entries are
 *            ignored
 * @param shouldCutOffCalcColumn
 *            whether to clear the fixed calculation column (index 5) during
 *            extraction
 */
public record OdsParseConfig(Predicate<String> tableNameConvention, String startMarker, List<String> splitMarkers,
        boolean shouldCutOffCalcColumn) {
    public OdsParseConfig {
        Objects.requireNonNull(tableNameConvention, "tableNameConvention must not be null");
        splitMarkers = splitMarkers == null ? List.of()
                : splitMarkers.stream().filter(marker -> marker != null && !marker.isBlank()).toList();
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
        this(tableNameConvention, startMarker, List.of(), shouldCutOffCalcColumn);
    }
}