package ch.puzzle.pctsmigration.ods;

import java.util.List;

public record OdsParseResult(List<Sheet> sheets) {

    public record Sheet(String name, List<List<String>> rows) {}
}
