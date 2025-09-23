package ch.puzzle.pcts.dto.degree_type;

import java.math.BigDecimal;

public record DegreeTypeDto(Long degreeTypeId, String name, BigDecimal highlyRelevantPoints,
        BigDecimal limitedRelevantPoints, BigDecimal littleRelevantPoints) {
}
