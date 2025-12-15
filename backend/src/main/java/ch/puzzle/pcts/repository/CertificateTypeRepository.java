package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CertificateTypeRepository extends SoftDeleteRepository<CertificateType, Long> {
    List<CertificateType> findByCertificateKindNotAndDeletedAtIsNull(CertificateKind certificatekind);
    List<CertificateType> findByCertificateKindAndDeletedAtIsNull(CertificateKind certificatekind);

    Optional<CertificateType> findByCertificateKindAndName(CertificateKind certificateKind, String name);
    Optional<CertificateType> findByCertificateKindNotAndName(CertificateKind certificateKind, String name);

    Optional<CertificateType> findByIdAndCertificateKindAndDeletedAtIsNull(Long id, CertificateKind certificateKind);
    Optional<CertificateType> findByIdAndCertificateKindNotAndDeletedAtIsNull(Long id, CertificateKind certificateKind);

    @Transactional
    void deleteCertificateTypeByIdAndCertificateKind(Long id, CertificateKind certificateKind);

    @Transactional
    void deleteCertificateTypeByIdAndCertificateKindNot(Long id, CertificateKind certificateKind);
}
