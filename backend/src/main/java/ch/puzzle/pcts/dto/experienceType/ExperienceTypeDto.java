package ch.puzzle.pcts.dto.experienceType;

import java.math.BigDecimal;

public record ExperienceTypeDto(Long id, String name, BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
        BigDecimal littleRelevantPoints) {
}
