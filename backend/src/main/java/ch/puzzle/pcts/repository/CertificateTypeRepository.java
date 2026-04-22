package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.certificatetype.CertificateType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateTypeRepository extends SoftDeleteRepository<CertificateType, Long> {
    Optional<CertificateType> findByNameAndDeletedAtIsNull(String name);

    List<CertificateType> findAllByLinkNotNullAndDeletedAtIsNull();

    boolean existsByNameAndPublisherAndIdNot(String name, String publisher, Long id);

    default Optional<CertificateType> findByNameOfCertificateType(String name) {
        return findByNameAndDeletedAtIsNull(name);
    }
}
