package ch.puzzle.pcts.dto.degree_type;

import java.math.BigDecimal;

public record DegreeTypeWithoutIdDto(String name, BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
        BigDecimal littleRelevantPoints) {
}
