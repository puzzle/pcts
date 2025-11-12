package ch.puzzle.pcts.dto.membercertificate;

import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import java.time.LocalDate;

public record MemberCertificateDto(Long id, MemberDto member, CertificateTypeDto certificate, LocalDate completed_at,
        LocalDate valid_until, String comment) {

}
