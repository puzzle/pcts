package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.CERTIFICATE_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import ch.puzzle.pcts.util.TestData;
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
        return new CertificateType(null,
                                   "Created certificate type",
                                   BigDecimal.valueOf(3),
                                   "This is a newly created certificate",
                                   Set.of(new Tag(1L, "Important tag")));
    }

    @Override
    List<CertificateType> getAll() {
        return TestData.CERTIFICATE_TYPES;
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
        Long id = 2L;

        persistenceService.delete(id);

        assertThatThrownBy(() -> persistenceService.getById(id)).isInstanceOf(PCTSException.class);
    }

    @DisplayName("Should update certificate type")
    @Test
    void shouldUpdateCertificate() {
        Long cId = 4L;
        CertificateType updatePayload = new CertificateType(cId,
                                                            "Updated certificate type",
                                                            BigDecimal.valueOf(3),
                                                            "This is a updated certificate",
                                                            Set
                                                                    .of(new Tag(null, "Important tag"),
                                                                        new Tag(null, "Way more important tag")));

        persistenceService.save(updatePayload);

        CertificateType certificateResult = persistenceService.getById(cId);
        assertThat(certificateResult.getId()).isEqualTo(cId);
        assertThat(certificateResult.getName()).isEqualTo("Updated certificate type");
        assertThat(certificateResult.getPoints()).isEqualByComparingTo(BigDecimal.valueOf(3));
        assertThat(certificateResult.getComment()).isEqualTo("This is a updated certificate");
        assertThat(certificateResult.getCertificateKind()).isEqualTo(CertificateKind.CERTIFICATE);
        assertThat(certificateResult.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("Important tag", "Way more important tag");
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
        Long certificateId = 1L;

        CertificateType result = persistenceService.getById(certificateId);

        assertThat(result.getId()).isEqualTo(certificateId);
        assertThat(result.getName()).isEqualTo("Certificate Type 1");
        assertThat(result.getCertificateKind()).isEqualTo(CertificateKind.CERTIFICATE);
    }

    @DisplayName("Should not get leadership experience with certificate method")
    @Test
    void shouldNotGetLeadershipExperienceAsCertificate() {
        Long id = 5L;

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
                                                     CERTIFICATE_TYPE)));
    }
}