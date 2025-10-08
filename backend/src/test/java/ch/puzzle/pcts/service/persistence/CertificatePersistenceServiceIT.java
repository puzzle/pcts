package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.repository.CertificateRepository;
import java.math.BigDecimal;
import java.util.Set;

class CertificatePersistenceServiceIT
        extends
            PersistenceBaseIT<Certificate, CertificateRepository, CertificatePersistenceService> {

    CertificatePersistenceServiceIT(CertificatePersistenceService service) {
        super(service);
    }

    @Override
    Certificate getCreateEntity() {
        return new Certificate(null,
                               "Created certificate",
                               BigDecimal.valueOf(3),
                               "This is a newly created certificate",
                               Set.of(new Tag(1L, "Important tag"), new Tag(2L, "Way more important tag")));
    }

    @Override
    Certificate getUpdateEntity() {
        return new Certificate(null,
                               "Updated certificate",
                               BigDecimal.valueOf(3),
                               "This is a updated certificate",
                               Set.of(new Tag(null, "Important tag"), new Tag(null, "Way more important tag")));
    }

    @Override
    Long getId(Certificate certificate) {
        return certificate.getId();
    }

    @Override
    void setId(Certificate certificate, Long id) {
        certificate.setId(id);
    }
}
