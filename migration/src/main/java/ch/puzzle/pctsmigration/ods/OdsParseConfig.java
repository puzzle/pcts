package ch.puzzle.pctsmigration.ods;

import java.util.List;

public record OdsParseConfig(List<String> tableNames, String startMarker) {
}
