package ch.puzzle.pctsmigration.degree;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DegreeWrapper(@Valid @NotNull List<DegreeAiResultDto> items) {
}
