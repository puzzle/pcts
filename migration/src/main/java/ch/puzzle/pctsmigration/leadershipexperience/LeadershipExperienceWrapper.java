package ch.puzzle.pctsmigration.leadershipexperience;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record LeadershipExperienceWrapper (@Valid @NotNull List<LeadershipExperienceAiResultDto> items) {

}
