package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_TYPE;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
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
        return LEADERSHIP_TYPE_1;
    }

    @Override
    List<CertificateType> getAll() {
        return LEADERSHIP_EXPERIENCE_TYPES;
    }

    @Override
    @DisplayName("Should delete entity")
    @Test
    void shouldDelete() {
        persistenceService.delete(LEADERSHIP_TYPE_1_ID);

        assertThatThrownBy(() -> persistenceService.getById(LEADERSHIP_TYPE_1_ID))
                .isInstanceOf(PCTSException.class)
                .hasMessage("404 NOT_FOUND");
    }

    @Override
    @DisplayName("Should get entity by id")
    @Test
    void shouldGetEntityById() {
        CertificateType entity = assertDoesNotThrow(() -> persistenceService.getById(LEADERSHIP_TYPE_1_ID));

        assertEquals(LEADERSHIP_TYPE_1_ID, entity.getId());
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
        CertificateType updatePayload = CertificateType.Builder
                .builder()
                .withId(LEADERSHIP_TYPE_1_ID)
                .withName("Updated leadership experience type")
                .withPoints(BigDecimal.valueOf(5))
                .withComment("This is a updated leadership experience type")
                .withCertificateKind(CertificateKind.YOUTH_AND_SPORT)
                .withTags(Set.of(TAG_1))
                .build();

        persistenceService.save(updatePayload);

        CertificateType leadershipResult = assertDoesNotThrow(() -> persistenceService.getById(LEADERSHIP_TYPE_1_ID));
        assertThat(leadershipResult.getId()).isEqualTo(LEADERSHIP_TYPE_1_ID);
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
        CertificateType leadership = assertDoesNotThrow(() -> persistenceService.getById(LEADERSHIP_TYPE_1_ID));

        assertEquals(LEADERSHIP_TYPE_1_ID, leadership.getId());
        assertEquals(LEADERSHIP_TYPE_1.getName(), leadership.getName());
        assertTrue(leadership.getCertificateKind().isLeadershipExperienceType());
    }

    @DisplayName("Should not get certificate with leadership experience method")
    @Test
    void shouldNotGetCertificateAsLeadershipExperience() {
        assertThatThrownBy(() -> persistenceService.getById(INVALID_ID))
                .isInstanceOf(PCTSException.class)
                .extracting("errorKeys", "errorAttributes")
                .containsExactly(List.of(ErrorKey.NOT_FOUND),
                                 List
                                         .of(Map
                                                 .of(FieldKey.FIELD,
                                                     "id",
                                                     FieldKey.IS,
                                                     INVALID_ID.toString(),
                                                     FieldKey.ENTITY,
                                                     LEADERSHIP_EXPERIENCE_TYPE)));
    }
}