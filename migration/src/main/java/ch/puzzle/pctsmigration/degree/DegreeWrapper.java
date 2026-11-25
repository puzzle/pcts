package ch.puzzle.pctsmigration.degree;

import ch.puzzle.pctsmigration.ai.AiResultWrapper;

import java.util.List;

public class DegreeWrapper extends AiResultWrapper<DegreeAiResultDto> {

    public DegreeWrapper() {
        super(null);
    }

    public DegreeWrapper(List<DegreeAiResultDto> items) {
        super(items);
    }
}
