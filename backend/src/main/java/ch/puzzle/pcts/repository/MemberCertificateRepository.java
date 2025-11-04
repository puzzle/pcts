package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberCertificateRepository extends SoftDeleteRepository<MemberCertificate, Long> {
}
