package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.CERTIFICATE_TYPE;
import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
class CertificateTypePersistenceServiceIT
        extends
            PersistenceBaseIT<CertificateType, CertificateTypeRepository, CertificateTypePersistenceService> {

    @Autowired
    CertificateTypePersistenceServiceIT(CertificateTypePersistenceService service) {
        super(service);
    }

    @Override
    CertificateType getModel() {
        return CERT_TYPE_2;
    }

    @Override
    List<CertificateType> getAll() {
        return CERTIFICATE_TYPES;
    }

    @Override
    @DisplayName("Should get all entities")
    @Test
    void shouldGetAllEntities() {
        List<CertificateType> expectedCertificates = getAll()
                .stream()
                .filter(ct -> ct.getCertificateKind() == CertificateKind.CERTIFICATE)
                .toList();

        List<CertificateType> all = persistenceService.getAll();

        assertThat(all).hasSize(4).usingRecursiveComparison().ignoringFields("tags").isEqualTo(expectedCertificates);
    }

    @Override
    @DisplayName("Should delete entity")
    @Test
    void shouldDelete() {
        persistenceService.delete(CERTIFICATE_2_ID);

        assertThatThrownBy(() -> persistenceService.getById(CERTIFICATE_2_ID)).isInstanceOf(PCTSException.class);
    }

    @DisplayName("Should update certificate type")
    @Test
    void shouldUpdateCertificate() {
        CertificateType updatePayload = CertificateType.Builder
                .builder()
                .withId(CERTIFICATE_4_ID)
                .withName("Updated certificate type")
                .withPoints(BigDecimal.valueOf(3))
                .withComment("This is a updated certificate")
                .withTags(Set.of(TAG_3, TAG_4))
                .withCertificateKind(CertificateKind.CERTIFICATE)
                .build();

        persistenceService.save(updatePayload);

        CertificateType certificateResult = persistenceService.getById(CERTIFICATE_4_ID);
        assertThat(certificateResult.getId()).isEqualTo(CERTIFICATE_4_ID);
        assertThat(certificateResult.getName()).isEqualTo("Updated certificate type");
        assertThat(certificateResult.getPoints()).isEqualByComparingTo(BigDecimal.valueOf(3));
        assertThat(certificateResult.getComment()).isEqualTo("This is a updated certificate");
        assertThat(certificateResult.getCertificateKind()).isEqualTo(CertificateKind.CERTIFICATE);
        assertThat(certificateResult.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder(TAG_3.getName(), TAG_4.getName());
    }

    @DisplayName("Should get all certificate types")
    @Test
    void shouldGetAllCertificateTypes() {
        List<CertificateType> all = persistenceService.getAll();

        assertThat(all).hasSize(4);
        assertThat(all)
                .extracting(CertificateType::getName)
                .containsExactlyInAnyOrder("Certificate Type 1",
                                           "Certificate Type 2",
                                           "Certificate Type 3",
                                           "Certificate Type 4");
    }

    @DisplayName("Should get certificate type by id")
    @Test
    void shouldGetCertificateTypeById() {
        CertificateType result = persistenceService.getById(CERTIFICATE_1_ID);

        assertThat(result.getId()).isEqualTo(CERTIFICATE_1_ID);
        assertThat(result.getName()).isEqualTo("Certificate Type 1");
        assertThat(result.getCertificateKind()).isEqualTo(CertificateKind.CERTIFICATE);
    }

    @DisplayName("Should not get leadership experience with certificate method")
    @Test
    void shouldNotGetLeadershipExperienceAsCertificate() {
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
                                                     CERTIFICATE_TYPE)));
    }
}