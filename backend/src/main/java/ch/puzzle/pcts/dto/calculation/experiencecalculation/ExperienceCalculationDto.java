package ch.puzzle.pcts.dto.calculation.experiencecalculation;

import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.model.calculation.Relevancy;

public record ExperienceCalculationDto(Long id, ExperienceDto experience, String comment, Relevancy relevancy) {
}