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

    List<CertificateType> findAllByCertificateKindAndLinkNotNullAndDeletedAtIsNull(CertificateKind kind);

    boolean existsByNameAndPublisherAndIdNot(String name, String publisher, Long id);

    default List<CertificateType> findAllOfCertificateType() {
        return findAllByCertificateKindAndDeletedAtIsNull(CertificateKind.CERTIFICATE);
    }

    default List<CertificateType> findAllOfLeadershipExperienceType() {
        return findAllByCertificateKindNotAndDeletedAtIsNull(CertificateKind.CERTIFICATE);
    }

    default Optional<CertificateType> findByIdOfCertificateType(Long id) {
        return findByIdAndCertificateKindAndDeletedAtIsNull(id, CertificateKind.CERTIFICATE);
    }

    default Optional<CertificateType> findByIdOfLeadershipExperienceType(Long id) {
        return findByIdAndCertificateKindNotAndDeletedAtIsNull(id, CertificateKind.CERTIFICATE);
    }

    default Optional<CertificateType> findByNameOfCertificateType(String name) {
        return findByNameAndCertificateKindAndDeletedAtIsNull(name, CertificateKind.CERTIFICATE);
    }

    default Optional<CertificateType> findByNameOfLeadershipExperienceType(String name) {
        return findByNameAndCertificateKindNotAndDeletedAtIsNull(name, CertificateKind.CERTIFICATE);
    }
}