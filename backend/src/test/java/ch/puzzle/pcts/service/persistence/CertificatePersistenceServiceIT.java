package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.certificate.Tag;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CertificatePersistenceServiceIT extends PersistenceBasicIT {

    @Autowired
    private CertificatePersistenceService persistenceService;

    @Autowired
    private TagPersistenceService tagPersistenceService;

    @DisplayName("Should get certificate and leadership experience by id")
    @Test
    void shouldGetById() {
        Optional<Certificate> certificate = persistenceService.getById(3L);

        assertThat(certificate).isPresent();
        assertThat(certificate.get().getId()).isEqualTo(3L);
        assertThat(certificate.get().getName()).isEqualTo("Certificate 3");
        assertThat(certificate.get().getPoints()).isEqualByComparingTo(BigDecimal.valueOf(3));
        assertThat(certificate.get().getComment()).isEqualTo("This is Certificate 3");
        assertThat(certificate.get().getCertificateType()).isEqualTo(CertificateType.CERTIFICATE);

    }

    @DisplayName("Should get all certificates")
    @Test
    void shouldGetAllCertificates() {
        List<Certificate> all = persistenceService.getAllCertificates();

        assertThat(all).hasSize(4);
        assertThat(all)
                .extracting(Certificate::getName)
                .containsExactlyInAnyOrder("Certificate 1", "Certificate 2", "Certificate 3", "Certificate 4");
    }

    @DisplayName("Should get all leadership experiences")
    @Test
    void shouldGetAllLeadershipExperiences() {
        List<Certificate> all = persistenceService.getAllLeadershipExperiences();

        assertThat(all).hasSize(3);
        assertThat(all)
                .extracting(Certificate::getName)
                .containsExactlyInAnyOrder("LeadershipExperience 1",
                                           "LeadershipExperience 2",
                                           "LeadershipExperience 3");
    }

    @DisplayName("Should create a certificate and a leadership experience")
    @Transactional
    @Test
    void shouldCreate() {
        Certificate certificate = new Certificate(null,
                                                  "Created certificate",
                                                  BigDecimal.valueOf(3),
                                                  "This is a newly created certificate",
                                                  Set
                                                          .of(new Tag(1L, "Important tag"),
                                                              new Tag(2L, "Way more important tag")));

        Certificate leadershipExperience = new Certificate(null,
                                                           "Created leadership experience",
                                                           BigDecimal.valueOf(3),
                                                           "This is a newly created leadership experience",
                                                           CertificateType.LEADERSHIP_TRAINING);

        Certificate createdCertificate = persistenceService.create(certificate);
        Certificate createdLeadershipExperience = persistenceService.create(leadershipExperience);

        assertThat(createdCertificate.getId()).isEqualTo(8L);
        assertThat(createdCertificate.getName()).isEqualTo(certificate.getName());
        assertThat(createdCertificate.getPoints()).isEqualByComparingTo(certificate.getPoints());
        assertThat(createdCertificate.getComment()).isEqualTo(certificate.getComment());
        assertThat(createdCertificate.getTags()).isEqualTo(certificate.getTags());
        assertThat(createdCertificate.getCertificateType()).isEqualTo(CertificateType.CERTIFICATE);

        assertThat(createdLeadershipExperience.getId()).isEqualTo(9L);
        assertThat(createdLeadershipExperience.getName()).isEqualTo(leadershipExperience.getName());
        assertThat(createdLeadershipExperience.getPoints()).isEqualByComparingTo(leadershipExperience.getPoints());
        assertThat(createdLeadershipExperience.getComment()).isEqualTo(leadershipExperience.getComment());
        assertThat(createdLeadershipExperience.getCertificateType()).isEqualTo(CertificateType.LEADERSHIP_TRAINING);
    }

    @DisplayName("Should update certificate and leadership experience")
    @Transactional
    @Test
    void shouldUpdate() {
        Long cId = 4L;
        Long lId = 5L;

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

        persistenceService.update(cId, certificate);
        persistenceService.update(lId, leadershipExperience);
        Optional<Certificate> certificateResult = persistenceService.getById(cId);
        Optional<Certificate> leadershipResult = persistenceService.getById(lId);

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

    @DisplayName("Should delete certificate")
    @Transactional
    @Test
    void shouldDelete() {
        Long id = 2L;

        persistenceService.delete(id);

        Optional<Certificate> result = persistenceService.getById(id);
        assertThat(result).isNotPresent();
    }
}
