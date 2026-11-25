package ch.puzzle.pctsmigration.experience;

import ch.puzzle.pctsmigration.ai.AiResultWrapper;

import java.util.List;

public class ExperienceWrapper extends AiResultWrapper<ExperienceAiResultDto> {

    public ExperienceWrapper() {
        super(null);
    }

    public ExperienceWrapper(List<ExperienceAiResultDto> items) {
        super(items);
    }
}