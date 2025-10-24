package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long>, SoftDeleteRepository<Certificate> {
    List<Certificate> findByCertificateTypeNotAndDeletedAtIsNull(CertificateType certificateType);

    List<Certificate> findByCertificateTypeAndDeletedAtIsNull(CertificateType certificateType);
}
