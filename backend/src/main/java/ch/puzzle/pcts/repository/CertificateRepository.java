package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends SoftDeleteRepository<Certificate, Long> {
    List<Certificate> findByCertificateTypeNotAndDeletedAtIsNull(CertificateType certificateType);

    List<Certificate> findByCertificateTypeAndDeletedAtIsNull(CertificateType certificateType);
}
