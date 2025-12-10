package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class LeadershipTypePersistenceServiceIT
        extends
            PersistenceBaseIT<CertificateType, CertificateTypeRepository, LeadershipTypePersistenceService> {

    @Autowired
    LeadershipTypePersistenceServiceIT(LeadershipTypePersistenceService service) {
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

    @Override
    @DisplayName("Should delete entity")
    @Transactional
    @Test
    void shouldDelete() {
        Long id = 5L;

        service.delete(id);

        Optional<CertificateType> result = service.getById(id);
        assertThat(result).isNotPresent();
    }

    @Override
    @DisplayName("Should get all entities")
    @Transactional
    @Test
    void shouldGetAllEntities() {
        List<CertificateType> all = service.getAll();
        assertThat(all).hasSize(3).containsExactlyElementsOf(getAll().subList(4, 7));
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

    @DisplayName("Should get all leadership experience types")
    @Test
    void shouldGetAllLeadershipExperienceTypes() {
        List<CertificateType> all = service.getAll();

        assertThat(all).hasSize(3);
        assertThat(all)
                .extracting(CertificateType::getName)
                .containsExactlyInAnyOrder("LeadershipExperience Type 1",
                                           "LeadershipExperience Type 2",
                                           "LeadershipExperience Type 3");
    }

    @DisplayName("Should get leadership experience type by id")
    @Test
    void shouldGetLeadershipExperienceTypeById() {
        Long leadershipId = 5L;

        Optional<CertificateType> leadership = service.getById(leadershipId);

        // todo make this cleaner
        assertThat(leadership).isNotNull();
        assertThat(leadership.get().getId()).isEqualTo(leadershipId);
        assertThat(leadership.get().getName()).isEqualTo("LeadershipExperience Type 1");
        assertThat(leadership.get().getCertificateKind()).isNotEqualTo(CertificateKind.CERTIFICATE);
    }

    @DisplayName("Should not get certificate with leadership experience method")
    @Test
    void shouldNotGetCertificateAsLeadershipExperience() {
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.getById(id));

        assertEquals(List.of(ErrorKey.NOT_FOUND), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "id",
                            FieldKey.IS,
                            id.toString(),
                            FieldKey.ENTITY,
                            LEADERSHIP_EXPERIENCE_TYPE)),
                     exception.getErrorAttributes());
    }
}
