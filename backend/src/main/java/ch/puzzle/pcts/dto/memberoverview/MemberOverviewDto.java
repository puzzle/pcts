package ch.puzzle.pcts.dto.memberoverview;

import ch.puzzle.pcts.dto.member.MemberDto;

public record MemberOverviewDto(MemberDto member, MemberCvDto cv) {
}
