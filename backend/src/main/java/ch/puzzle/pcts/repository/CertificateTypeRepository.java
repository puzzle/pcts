package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateTypeRepository extends SoftDeleteRepository<CertificateType, Long> {
    List<CertificateType> findByCertificateKindNotAndDeletedAtIsNull(CertificateKind certificatekind);
    List<CertificateType> findByCertificateKindAndDeletedAtIsNull(CertificateKind certificatekind);

    Optional<CertificateType> findByCertificateKindAndName(CertificateKind certificateKind, String name);
    Optional<CertificateType> findByCertificateKindNotAndName(CertificateKind certificateKind, String name);

    Optional<CertificateType> findByIdAndCertificateKind(Long id, CertificateKind certificateKind);
    Optional<CertificateType> findByIdAndCertificateKindNot(Long id, CertificateKind certificateKind);

    Optional<CertificateType> deleteCertificateTypeByIdAndCertificateKind(Long id, CertificateKind certificateKind);
    Optional<CertificateType> deleteCertificateTypeByIdAndCertificateKindNot(Long id, CertificateKind certificateKind);
}
