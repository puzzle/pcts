package ch.puzzle.pcts.dto.memberoverview;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import java.util.List;

public record MemberOverviewDto(MemberDto member, MemberCvDto cv, List<CalculationDto> calculations) {
}
