package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByCertificateTypeNot(CertificateType certificateType);

    List<Certificate> findByCertificateType(CertificateType certificateType);

    @Query(value = "SELECT * FROM certificate WHERE deleted_at IS NULL AND id = :id", nativeQuery = true)
    Optional<Certificate> findById(Long id);

    @Query(value = "SELECT * FROM certificate WHERE deleted_at IS NULL", nativeQuery = true)
    List<Certificate> findAll();
}
