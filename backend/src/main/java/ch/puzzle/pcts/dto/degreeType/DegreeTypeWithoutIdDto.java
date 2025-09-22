package ch.puzzle.pcts.dto.degreeType;

import java.math.BigDecimal;

public record DegreeTypeWithoutIdDto(String name, BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
        BigDecimal littleRelevantPoints) {
}
