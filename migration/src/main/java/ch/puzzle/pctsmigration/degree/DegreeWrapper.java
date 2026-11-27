package ch.puzzle.pctsmigration.degree;

import ch.puzzle.pctsmigration.ai.AiResultWrapper;

import java.util.List;

public record DegreeWrapper(List<DegreeAiResultDto> items) implements AiResultWrapper<DegreeAiResultDto> {
}