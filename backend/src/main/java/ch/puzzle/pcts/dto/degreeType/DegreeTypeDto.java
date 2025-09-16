package ch.puzzle.pcts.dto.degreeType;

import java.math.BigDecimal;

public record DegreeTypeDto(Long id, String name, BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
        BigDecimal littleRelevantPoints) {
}
