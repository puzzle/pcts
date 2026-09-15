// package ch.puzzle.pctsmigration.ods;
//
// import static org.assertj.core.api.Assertions.assertThat;
//
// import java.util.List;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
//
// class SheetRowCollectorTest {
//
// @Test
// @DisplayName("When initialized without a start marker, it starts recording
// immediately and never stops on empty rows")
// void withoutMarker_startsImmediatelyAndNeverStops() {
// SheetRowCollector collector = new SheetRowCollector(null);
//
// collector.processRow(List.of("Row 1"));
// collector.processRow(List.of(""));
// collector.processRow(List.of("", ""));
// collector.processRow(List.of(""));
// collector.processRow(List.of("Row 2"));
//
// assertThat(collector.isDone()).isFalse();
//
// assertThat(collector.getCollectedRows()).containsExactly(List.of("Row 1"),
// List.of("Row 2"));
// }
//
// @Test
// @DisplayName("When initialized with blank start marker, behaves same as null
// marker")
// void withBlankMarker_startsImmediately() {
// SheetRowCollector collector = new SheetRowCollector(" ");
//
// collector.processRow(List.of("Data"));
//
// assertThat(collector.getCollectedRows()).containsExactly(List.of("Data"));
// assertThat(collector.isDone()).isFalse();
// }
//
// @Test
// @DisplayName("When initialized with a start marker, ignores all rows until
// the marker is found")
// void withMarker_ignoresRowsUntilFound() {
// SheetRowCollector collector = new SheetRowCollector("START");
//
// collector.processRow(List.of("Ignore me"));
// collector.processRow(List.of(""));
// collector.processRow(List.of("Still ignore"));
//
// assertThat(collector.getCollectedRows()).isEmpty();
//
// collector.processRow(List.of("Col1", "START HERE", "Col3"));
// collector.processRow(List.of("Data 1"));
//
// assertThat(collector.getCollectedRows())
// .containsExactly(List.of("Col1", "START HERE", "Col3"), List.of("Data 1"));
// }
//
// @Test
// @DisplayName("Stops recording after 3 consecutive empty rows (only if a
// marker was configured)")
// void withMarker_stopsAfterThreeConsecutiveEmptyRows() {
// SheetRowCollector collector = new SheetRowCollector("START");
//
// collector.processRow(List.of("START"));
// assertThat(collector.isDone()).isFalse();
//
// collector.processRow(List.of(""));
// assertThat(collector.isDone()).isFalse();
//
// collector.processRow(List.of("", ""));
// assertThat(collector.isDone()).isFalse();
//
// collector.processRow(List.of(""));
// assertThat(collector.isDone()).isTrue();
//
// assertThat(collector.getCollectedRows()).containsExactly(List.of("START"));
// }
//
// @Test
// @DisplayName("Resets the empty row counter when a valid row is processed")
// void resetsEmptyRowCount_onValidRow() {
// SheetRowCollector collector = new SheetRowCollector("START");
//
// collector.processRow(List.of("START"));
//
// collector.processRow(List.of(""));
// collector.processRow(List.of(""));
//
// collector.processRow(List.of("Valid Data"));
//
// collector.processRow(List.of(""));
// collector.processRow(List.of(""));
//
// assertThat(collector.isDone()).isFalse();
//
// assertThat(collector.getCollectedRows()).containsExactly(List.of("START"),
// List.of("Valid Data"));
// }
// }