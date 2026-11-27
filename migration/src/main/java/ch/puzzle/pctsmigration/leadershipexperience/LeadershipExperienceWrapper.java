package ch.puzzle.pctsmigration.leadershipexperience;

import ch.puzzle.pctsmigration.ai.AiResultWrapper;

import java.util.List;

public record LeadershipExperienceWrapper(List<LeadershipExperienceAiResultDto> items) implements AiResultWrapper<LeadershipExperienceAiResultDto> {
}