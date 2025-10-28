package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends SoftDeleteRepository<Certificate, Long> {
    List<Certificate> findByCertificateTypeNotAndDeletedAtIsNull(CertificateType certificateType);

    List<Certificate> findByCertificateTypeAndDeletedAtIsNull(CertificateType certificateType);

    Optional<Certificate> findByName(String name);
}
