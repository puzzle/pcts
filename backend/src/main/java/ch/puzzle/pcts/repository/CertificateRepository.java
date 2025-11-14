package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.mode.certificate.Certificate;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends SoftDeleteRepository<Certificate, Long> {
}
