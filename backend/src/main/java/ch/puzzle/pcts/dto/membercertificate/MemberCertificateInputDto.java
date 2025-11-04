package ch.puzzle.pcts.dto.membercertificate;

import java.util.Date;

public record MemberCertificateInputDto(Long memberId, Long certificateId, Date completed_at, Date valid_until,
        String comment) {

}
