package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

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
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class LeadershipTypePersistenceServiceIT
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
    @Test
    void shouldDelete() {
        Long id = 6L;

        persistenceService.delete(id);

        assertThatThrownBy(() -> persistenceService.getById(id))
                .isInstanceOf(PCTSException.class)
                .hasMessage("404 NOT_FOUND");
    }

    @Override
    @DisplayName("Should get entity by id")
    @Test
    void shouldGetEntityById() {
        CertificateType entity = assertDoesNotThrow(() -> persistenceService.getById(7L));

        assertEquals(7L, entity.getId());
    }

    @Override
    @DisplayName("Should get all entities")
    @Test
    void shouldGetAllEntities() {
        List<CertificateType> expectedLeadershipTypes = getAll()
                .stream()
                .filter(ct -> ct.getCertificateKind() != null && ct.getCertificateKind().isLeadershipExperienceType())
                .toList();

        List<CertificateType> all = persistenceService.getAll();

        assertThat(all).hasSize(3).containsExactlyElementsOf(expectedLeadershipTypes);
    }

    @Override
    @DisplayName("Should update leadership experience type")
    @Test
    void shouldUpdate() {
        Long lId = 6L;
        CertificateType updatePayload = new CertificateType(lId,
                                                            "Updated leadership experience type",
                                                            BigDecimal.valueOf(5),
                                                            "This is a updated leadership experience type",
                                                            CertificateKind.YOUTH_AND_SPORT);

        persistenceService.save(updatePayload);

        CertificateType leadershipResult = assertDoesNotThrow(() -> persistenceService.getById(lId));
        assertThat(leadershipResult.getId()).isEqualTo(lId);
        assertThat(leadershipResult.getName()).isEqualTo("Updated leadership experience type");
        assertThat(leadershipResult.getPoints()).isEqualByComparingTo(BigDecimal.valueOf(5));
        assertThat(leadershipResult.getComment()).isEqualTo("This is a updated leadership experience type");
        assertThat(leadershipResult.getCertificateKind()).isEqualTo(CertificateKind.YOUTH_AND_SPORT);
    }

    @DisplayName("Should get all leadership experience types")
    @Test
    void shouldGetAllLeadershipExperienceTypes() {
        List<CertificateType> all = persistenceService.getAll();

        assertThat(all)
                .hasSize(3)
                .extracting(CertificateType::getName)
                .containsExactlyInAnyOrder("LeadershipExperience Type 1",
                                           "LeadershipExperience Type 2",
                                           "LeadershipExperience Type 3");
    }

    @DisplayName("Should get leadership experience type by id")
    @Test
    void shouldGetLeadershipExperienceTypeById() {
        Long leadershipId = 5L;

        CertificateType leadership = assertDoesNotThrow(() -> persistenceService.getById(leadershipId));

        assertEquals(leadershipId, leadership.getId());
        assertEquals("LeadershipExperience Type 1", leadership.getName());
        assertTrue(leadership.getCertificateKind().isLeadershipExperienceType());
    }

    @DisplayName("Should not get certificate with leadership experience method")
    @Test
    void shouldNotGetCertificateAsLeadershipExperience() {
        Long id = 1L;

        assertThatThrownBy(() -> persistenceService.getById(id))
                .isInstanceOf(PCTSException.class)
                .extracting("errorKeys", "errorAttributes")
                .containsExactly(List.of(ErrorKey.NOT_FOUND),
                                 List
                                         .of(Map
                                                 .of(FieldKey.FIELD,
                                                     "id",
                                                     FieldKey.IS,
                                                     id.toString(),
                                                     FieldKey.ENTITY,
                                                     LEADERSHIP_EXPERIENCE_TYPE)));
    }
}