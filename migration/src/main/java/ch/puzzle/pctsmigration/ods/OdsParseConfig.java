package ch.puzzle.pctsmigration.ods;

import java.util.function.Function;

public record OdsParseConfig(Function<String, Boolean> tableNameConvention, String startMarker) {
}
