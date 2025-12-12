package ch.puzzle.pcts.dto.calculation.calculationleadershipexperience;

import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;

public record LeadershipExperienceCalculationDto(Long id, String comment, LeadershipExperienceDto experience) {
}
