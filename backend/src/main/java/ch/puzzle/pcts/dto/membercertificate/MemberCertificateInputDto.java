package ch.puzzle.pcts.dto.membercertificate;

import java.time.LocalDate;

public record MemberCertificateInputDto(Long memberId, Long certificate_typeId, LocalDate completed_at,
        LocalDate valid_until, String comment) {

}
