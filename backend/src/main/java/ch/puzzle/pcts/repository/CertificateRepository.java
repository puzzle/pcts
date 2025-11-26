package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends SoftDeleteRepository<Certificate, Long> {

    List<Certificate> findAllByCertificateType_CertificateKindNot(CertificateKind certificateKind);

    Certificate findByCertificateType_CertificateKindNot(CertificateKind certificateKind);
}
