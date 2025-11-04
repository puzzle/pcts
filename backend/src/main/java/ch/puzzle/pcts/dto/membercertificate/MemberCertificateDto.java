package ch.puzzle.pcts.dto.membercertificate;

import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import java.util.Date;

public record MemberCertificateDto(Long id, MemberDto member, CertificateDto certificate, Date completed_at,
        Date valid_until, String comment) {

}
