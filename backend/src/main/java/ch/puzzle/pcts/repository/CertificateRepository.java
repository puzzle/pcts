package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends SoftDeleteRepository<Certificate, Long> {
    @Query("SELECT c FROM Certificate c WHERE c.id = :id AND c.certificateType.certificateKind <> :kind")
    Optional<Certificate> findByIdAndCertificateType_CertificateKindIsNot(@Param("id") Long id,
                                                                          @Param("kind") CertificateKind kind);
}
