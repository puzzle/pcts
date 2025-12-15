package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateTypeRepository extends SoftDeleteRepository<CertificateType, Long> {
    List<CertificateType> findAllByCertificateKindAndDeletedAtIsNull(CertificateKind kind);

    List<CertificateType> findAllByCertificateKindNotAndDeletedAtIsNull(CertificateKind kind);

    Optional<CertificateType> findByIdAndCertificateKindAndDeletedAtIsNull(Long id, CertificateKind kind);

    Optional<CertificateType> findByIdAndCertificateKindNotAndDeletedAtIsNull(Long id, CertificateKind kind);

    Optional<CertificateType> findByNameAndCertificateKindAndDeletedAtIsNull(String name, CertificateKind kind);

    Optional<CertificateType> findByNameAndCertificateKindNotAndDeletedAtIsNull(String name, CertificateKind kind);

    default List<CertificateType> findAllFromCertificateType() {
        return findAllByCertificateKindAndDeletedAtIsNull(CertificateKind.CERTIFICATE);
    }

    default List<CertificateType> findAllFromLeadershipExperienceType() {
        return findAllByCertificateKindNotAndDeletedAtIsNull(CertificateKind.CERTIFICATE);
    }

    default Optional<CertificateType> findByIdForCertificateType(Long id) {
        return findByIdAndCertificateKindAndDeletedAtIsNull(id, CertificateKind.CERTIFICATE);
    }

    default Optional<CertificateType> findByIdForLeadershipExperienceType(Long id) {
        return findByIdAndCertificateKindNotAndDeletedAtIsNull(id, CertificateKind.CERTIFICATE);
    }

    default Optional<CertificateType> findByNameFromCertificateType(String name) {
        return findByNameAndCertificateKindAndDeletedAtIsNull(name, CertificateKind.CERTIFICATE);
    }

    default Optional<CertificateType> findByNameFromLeadershipExperienceType(String name) {
        return findByNameAndCertificateKindNotAndDeletedAtIsNull(name, CertificateKind.CERTIFICATE);
    }
}