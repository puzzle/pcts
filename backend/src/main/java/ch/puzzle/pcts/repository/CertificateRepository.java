package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    Optional<Certificate> findByCertificateTypeNotAndId(CertificateType certificateType, Long id);

    List<Certificate> findByCertificateTypeNot(CertificateType certificateType);

    Optional<Certificate> findByCertificateTypeAndId(CertificateType certificateType, Long id);

    List<Certificate> findByCertificateType(CertificateType certificateType);

}
