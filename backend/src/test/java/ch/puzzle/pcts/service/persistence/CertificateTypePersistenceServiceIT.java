package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class CertificateTypePersistenceServiceIT
        extends
            PersistenceBaseIT<CertificateType, CertificateTypeRepository, CertificateTypePersistenceService> {

    @Autowired
    CertificateTypePersistenceServiceIT(CertificateTypePersistenceService service) {
        super(service);
    }

    @Override
    CertificateType getModel() {
        return new CertificateType(null,
                                   "Created certificate type",
                                   BigDecimal.valueOf(3),
                                   "This is a newly created certificate",
                                   Set.of(new Tag(1L, "Important tag")));
    }

    @Override
    List<CertificateType> getAll() {
        return List
                .of(new CertificateType(1L,
                                        "Certificate Type 1",
                                        BigDecimal.valueOf(5.5),
                                        "This is Certificate 1",
                                        Set.of(new Tag(1L, "Tag 1"))),
                    new CertificateType(2L,
                                        "Certificate Type 2",
                                        BigDecimal.valueOf(1),
                                        "This is Certificate 2",
                                        Set.of(new Tag(2L, "Longer tag name"))),
                    new CertificateType(3L,
                                        "Certificate Type 3",
                                        BigDecimal.valueOf(3),
                                        "This is Certificate 3",
                                        Set.of()),
                    new CertificateType(4L,
                                        "Certificate Type 4",
                                        BigDecimal.valueOf(0.5),
                                        "This is Certificate 4",
                                        Set.of()),
                    new CertificateType(5L,
                                        "LeadershipExperience Type 1",
                                        BigDecimal.valueOf(5.5),
                                        "This is LeadershipExperience 1",
                                        Set.of(),
                                        CertificateKind.MILITARY_FUNCTION),
                    new CertificateType(6L,
                                        "LeadershipExperience Type 2",
                                        BigDecimal.valueOf(1),
                                        "This is LeadershipExperience 2",
                                        Set.of(),
                                        CertificateKind.YOUTH_AND_SPORT),
                    new CertificateType(7L,
                                        "LeadershipExperience Type 3",
                                        BigDecimal.valueOf(3),
                                        "This is LeadershipExperience 3",
                                        Set.of(),
                                        CertificateKind.LEADERSHIP_TRAINING));
    }

    @DisplayName("Should update certificate type")
    @Transactional
    @Test
    void shouldUpdateCertificate() {
        Long cId = 4L;

        CertificateType certificate = new CertificateType(null,
                                                          "Updated certificate type",
                                                          BigDecimal.valueOf(3),
                                                          "This is a updated certificate",
                                                          Set
                                                                  .of(new Tag(null, "Important tag"),
                                                                      new Tag(null, "Way more important tag")));
        certificate.setId(cId);
        service.save(certificate);

        Optional<CertificateType> certificateResult = service.getById(cId);

        assertThat(certificateResult).isPresent();
        CertificateType updatedCertificate = certificateResult.get();

        assertThat(updatedCertificate.getId()).isEqualTo(cId);
        assertThat(updatedCertificate.getName()).isEqualTo("Updated certificate type");
        assertThat(updatedCertificate.getPoints()).isEqualByComparingTo(BigDecimal.valueOf(3));
        assertThat(updatedCertificate.getComment()).isEqualTo("This is a updated certificate");
        assertThat(updatedCertificate.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("Important tag", "Way more important tag");

        assertThat(updatedCertificate.getCertificateKind()).isEqualTo(CertificateKind.CERTIFICATE);
    }

    @DisplayName("Should update leadership experience type")
    @Transactional
    @Test
    void shouldUpdateLeadershipExperience() {
        Long lId = 5L;

        CertificateType leadershipExperience = new CertificateType(null,
                                                                   "Updated leadership experience type",
                                                                   BigDecimal.valueOf(5),
                                                                   "This is a updated leadership experience type",
                                                                   CertificateKind.YOUTH_AND_SPORT);
        leadershipExperience.setId(lId);
        service.save(leadershipExperience);

        Optional<CertificateType> leadershipResult = service.getById(lId);

        assertThat(leadershipResult).isPresent();
        CertificateType updatedLeadership = leadershipResult.get();

        assertThat(updatedLeadership.getId()).isEqualTo(lId);
        assertThat(updatedLeadership.getName()).isEqualTo("Updated leadership experience type");
        assertThat(updatedLeadership.getPoints()).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(updatedLeadership.getComment()).isEqualTo("This is a updated leadership experience type");
        assertThat(updatedLeadership.getCertificateKind()).isEqualTo(CertificateKind.YOUTH_AND_SPORT);
    }

    @DisplayName("Should get all certificate types")
    @Test
    void shouldGetAllCertificateTypes() {
        List<CertificateType> all = service.getAllCertificateTypes();

        assertThat(all).hasSize(4);
        assertThat(all)
                .extracting(CertificateType::getName)
                .containsExactlyInAnyOrder("Certificate Type 1",
                                           "Certificate Type 2",
                                           "Certificate Type 3",
                                           "Certificate Type 4");
    }

    @DisplayName("Should get all leadership experience types")
    @Test
    void shouldGetAllLeadershipExperienceTypes() {
        List<CertificateType> all = service.getAllLeadershipExperienceTypes();

        assertThat(all).hasSize(3);
        assertThat(all)
                .extracting(CertificateType::getName)
                .containsExactlyInAnyOrder("LeadershipExperience Type 1",
                                           "LeadershipExperience Type 2",
                                           "LeadershipExperience Type 3");
    }

    @DisplayName("Should get certificate type by id")
    @Test
    void shouldGetCertificateTypeById() {
        Long certificateId = 1L;

        Optional<CertificateType> result = service.getCertificateType(certificateId);

        assertThat(result).isPresent();

        CertificateType certificate = result.get();
        assertThat(certificate.getId()).isEqualTo(certificateId);
        assertThat(certificate.getName()).isEqualTo("Certificate Type 1");
        assertThat(certificate.getCertificateKind()).isEqualTo(CertificateKind.CERTIFICATE);
    }

    @DisplayName("Should not get leadership experience with certificate method")
    @Test
    void shouldNotGetLeadershipExperienceAsCertificate() {
        Long leadershipId = 5L;

        Optional<CertificateType> result = service.getCertificateType(leadershipId);

        assertThat(result).isEmpty();
    }

    @DisplayName("Should get leadership experience type by id")
    @Test
    void shouldGetLeadershipExperienceTypeById() {
        Long leadershipId = 5L;

        Optional<CertificateType> result = service.getLeadershipExperienceType(leadershipId);

        assertThat(result).isPresent();

        CertificateType leadership = result.get();
        assertThat(leadership.getId()).isEqualTo(leadershipId);
        assertThat(leadership.getName()).isEqualTo("LeadershipExperience Type 1");
        assertThat(leadership.getCertificateKind()).isNotEqualTo(CertificateKind.CERTIFICATE);
    }

    @DisplayName("Should not get certificate with leadership experience method")
    @Test
    void shouldNotGetCertificateAsLeadershipExperience() {
        Long certificateId = 1L;

        Optional<CertificateType> result = service.getLeadershipExperienceType(certificateId);

        assertThat(result).isEmpty();
    }

}
