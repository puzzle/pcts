package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.repository.CertificateRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class CertificatePersistenceServiceIT
        extends
            PersistenceBaseIT<Certificate, CertificateRepository, CertificatePersistenceService> {

    CertificatePersistenceService service;

    @Autowired
    CertificatePersistenceServiceIT(CertificatePersistenceService service) {
        super(service);
        this.service = service;
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
    List<Certificate> getAll() {
        return List
                .of(new Certificate(1L,
                                    "Certificate 1",
                                    BigDecimal.valueOf(5.5),
                                    "This is Certificate 1",
                                    Set.of(new Tag(1L, "Tag 1"))),
                    new Certificate(2L,
                                    "Certificate 2",
                                    BigDecimal.valueOf(1),
                                    "This is Certificate 2",
                                    Set.of(new Tag(2L, "Longer tag name"))),
                    new Certificate(3L, "Certificate 3", BigDecimal.valueOf(3), "This is Certificate 3", Set.of()),
                    new Certificate(4L, "Certificate 4", BigDecimal.valueOf(0.5), "This is Certificate 4", Set.of()));
    }

    @Override
    Long getId(Certificate certificate) {
        return certificate.getId();
    }

    @Override
    void setId(Certificate certificate, Long id) {
        certificate.setId(id);
    }

    @Override
    @DisplayName("Should update certificate")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long id = 4;
        Certificate certificate = getUpdateEntity();

        certificate.setId(id);
        service.save(certificate);
        Optional<Certificate> result = service.getById(id);

        assertThat(result).isPresent();

        Certificate updated = result.get();

        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getName()).isEqualTo("Updated certificate");
        assertThat(updated.getPoints()).isEqualByComparingTo(BigDecimal.valueOf(3));
        assertThat(updated.getComment()).isEqualTo("This is a updated certificate");
        assertThat(updated.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("Important tag", "Way more important tag");
    }
}
