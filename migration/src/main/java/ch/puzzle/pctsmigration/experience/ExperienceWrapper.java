package ch.puzzle.pctsmigration.experience;

import ch.puzzle.pctsmigration.ai.AiResultWrapper;

import java.util.List;

public record ExperienceWrapper(List<ExperienceAiResultDto> items) implements AiResultWrapper<ExperienceAiResultDto> {
}