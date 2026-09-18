package ch.puzzle.pctsmigration.ods;

import java.util.function.Predicate;

public record OdsParseConfig(Predicate<String> tableNameConvention, String startMarker, boolean shouldCutOfCalcRow) {
}
