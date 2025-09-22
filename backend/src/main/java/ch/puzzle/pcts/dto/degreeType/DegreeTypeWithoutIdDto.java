package ch.puzzle.pcts.dto.degreeType;

import java.math.BigDecimal;

public record DegreeTypeWithoutIdDto(String name, BigDecimal highly_relevant_points, BigDecimal limited_relevant_points,
        BigDecimal little_relevant_points) {

}
