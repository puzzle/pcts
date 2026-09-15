package ch.puzzle.pctsmigration.ods;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SheetRowCollectorTest {

    private Row createRow(String... cellTexts) {
        List<Cell> cells = Arrays.stream(cellTexts).map(text -> {
            Cell cell = new Cell(null);
            cell.setText(text);
            return cell;
        }).collect(Collectors.toList());

        return new Row(cells);
    }

    private List<List<String>> getCollectedRowTexts(SheetRowCollector collector) {
        return collector
                .getSheet()
                .getRows()
                .stream()
                .map(row -> row.getCells().stream().map(Cell::getText).collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("When initialized without a start marker, it starts recording immediately and never stops on empty rows")
    void withoutMarker_startsImmediatelyAndNeverStops() {
        SheetRowCollector collector = new SheetRowCollector(null);

        collector.processRow(createRow("Row 1"));
        collector.processRow(createRow(""));
        collector.processRow(createRow("", ""));
        collector.processRow(createRow(""));
        collector.processRow(createRow("Row 2"));

        assertThat(collector.isDone()).isFalse();

        assertThat(getCollectedRowTexts(collector)).containsExactly(List.of("Row 1"), List.of("Row 2"));
    }

    @Test
    @DisplayName("When initialized with blank start marker, behaves same as null marker")
    void withBlankMarker_startsImmediately() {
        SheetRowCollector collector = new SheetRowCollector(" ");

        collector.processRow(createRow("Data"));

        assertThat(getCollectedRowTexts(collector)).containsExactly(List.of("Data"));
        assertThat(collector.isDone()).isFalse();
    }

    @Test
    @DisplayName("When initialized with a start marker, ignores all rows until the marker is found")
    void withMarker_ignoresRowsUntilFound() {
        SheetRowCollector collector = new SheetRowCollector("START");

        collector.processRow(createRow("Ignore me"));
        collector.processRow(createRow(""));
        collector.processRow(createRow("Still ignore"));

        assertThat(getCollectedRowTexts(collector)).isEmpty();

        collector.processRow(createRow("Col1", "START HERE", "Col3"));
        collector.processRow(createRow("Data 1"));

        assertThat(getCollectedRowTexts(collector))
                .containsExactly(List.of("Col1", "START HERE", "Col3"), List.of("Data 1"));
    }

    @Test
    @DisplayName("Stops recording after 3 consecutive empty rows (only if a marker was configured)")
    void withMarker_stopsAfterThreeConsecutiveEmptyRows() {
        SheetRowCollector collector = new SheetRowCollector("START");

        collector.processRow(createRow("START"));
        assertThat(collector.isDone()).isFalse();

        collector.processRow(createRow(""));
        assertThat(collector.isDone()).isFalse();

        collector.processRow(createRow("", ""));
        assertThat(collector.isDone()).isFalse();

        collector.processRow(createRow(""));
        assertThat(collector.isDone()).isTrue();

        assertThat(getCollectedRowTexts(collector)).containsExactly(List.of("START"));
    }

    @Test
    @DisplayName("Resets the empty row counter when a valid row is processed")
    void resetsEmptyRowCount_onValidRow() {
        SheetRowCollector collector = new SheetRowCollector("START");

        collector.processRow(createRow("START"));

        collector.processRow(createRow(""));
        collector.processRow(createRow(""));

        collector.processRow(createRow("Valid Data"));

        collector.processRow(createRow(""));
        collector.processRow(createRow(""));

        assertThat(collector.isDone()).isFalse();

        assertThat(getCollectedRowTexts(collector)).containsExactly(List.of("START"), List.of("Valid Data"));
    }
}