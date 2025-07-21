package ch.puzzle.pcts.dto.example;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreateExampleDto(@NotEmpty @NotNull String text) {
}
