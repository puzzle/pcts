package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends SoftDeleteRepository<Certificate, Long> {
    Optional<Certificate> findByIdAndCertificateType(Long id);
}
