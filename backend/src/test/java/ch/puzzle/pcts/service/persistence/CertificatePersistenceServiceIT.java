package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.repository.CertificateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class CertificatePersistenceServiceIT
        extends
        PersistenceBaseIT<Certificate, CertificateRepository, CertificatePersistenceService> {

    private CertificatePersistenceService service;

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

    @DisplayName("Should update certificate and leadership experience")
    @Transactional
    @Test
    @Order(2)
    void shouldUpdate() {
        long cId = 4;
        long lId = 5;

        Certificate certificate = new Certificate(null,
                                                  "Updated certificate",
                                                  BigDecimal.valueOf(3),
                                                  "This is a updated certificate",
                                                  Set
                                                          .of(new Tag(null, "Important tag"),
                                                              new Tag(null, "Way more important tag")));

        Certificate leadershipExperience = new Certificate(null,
                                                           "Updated leadership experience",
                                                           BigDecimal.valueOf(5),
                                                           "This is a updated leadership experience",
                                                           CertificateType.YOUTH_AND_SPORT);
        service.update(cId, certificate);
        service.update(lId, leadershipExperience);
        Optional<Certificate> certificateResult = service.getById(cId);
        Optional<Certificate> leadershipResult = service.getById(lId);

        assertThat(certificateResult).isPresent();
        Certificate updatedCertificate = certificateResult.get();

        assertThat(updatedCertificate.getId()).isEqualTo(cId);
        assertThat(updatedCertificate.getName()).isEqualTo("Updated certificate");
        assertThat(updatedCertificate.getPoints()).isEqualByComparingTo(BigDecimal.valueOf(3));
        assertThat(updatedCertificate.getComment()).isEqualTo("This is a updated certificate");
        assertThat(updatedCertificate.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("Important tag", "Way more important tag");
        assertThat(certificate.getCertificateType()).isEqualTo(CertificateType.CERTIFICATE);

        assertThat(leadershipResult).isPresent();
        Certificate updatedLeadership = leadershipResult.get();

        assertThat(updatedLeadership.getId()).isEqualTo(lId);
        assertThat(updatedLeadership.getName()).isEqualTo("Updated leadership experience");
        assertThat(updatedLeadership.getPoints()).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(updatedLeadership.getComment()).isEqualTo("This is a updated leadership experience");
        assertThat(leadershipExperience.getCertificateType()).isEqualTo(CertificateType.YOUTH_AND_SPORT);
    }

    @DisplayName("Should get all certificates")
    @Test
    @Order(1)
    void shouldGetAllCertificates() {
        List<Certificate> all = service.getAllCertificates();

        assertThat(all).hasSize(4);
        assertThat(all)
                .extracting(Certificate::getName)
                .containsExactlyInAnyOrder("Certificate 1", "Certificate 2", "Certificate 3", "Certificate 4");
    }

    @DisplayName("Should get all leadership experiences")
    @Test
    @Order(1)
    void shouldGetAllLeadershipExperiences() {
        List<Certificate> all = service.getAllLeadershipExperiences();

        assertThat(all).hasSize(3);
        assertThat(all)
                .extracting(Certificate::getName)
                .containsExactlyInAnyOrder("LeadershipExperience 1",
                        "LeadershipExperience 2",
                        "LeadershipExperience 3");
    }
}
