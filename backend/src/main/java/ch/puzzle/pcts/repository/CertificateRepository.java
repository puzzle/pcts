package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.member.Member;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends SoftDeleteRepository<Certificate, Long> {
    List<Certificate> findAllByMember(Member member);
}
