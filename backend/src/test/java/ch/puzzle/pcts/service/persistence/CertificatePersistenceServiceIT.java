package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.repository.CertificateRepository;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import ch.puzzle.pcts.repository.MemberRepository;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CertificatePersistenceServiceIT
        extends
            PersistenceBaseIT<Certificate, CertificateRepository, CertificatePersistenceService> {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CertificateTypeRepository certificateTypeRepository;
    @Autowired
    private OrganisationUnitRepository organisationUnitRepository;

    @Autowired
    CertificatePersistenceServiceIT(CertificatePersistenceService service) {
        super(service);
    }

    @Override
    Certificate getModel() {
        return CERTIFICATE_2;
    }

    @Override
    List<Certificate> getAll() {
        return CERTIFICATES;
    }

    @Test
    @DisplayName("Should get leadership experience when kind is not CERTIFICATE")
    @Transactional
    void shouldGetLeadershipExperience() {
        Certificate certificate = Certificate.Builder
                .builder()
                .withMember(MEMBER_2)
                .withCertificateType(LEADERSHIP_TYPE_2)
                .withCompletedAt(LocalDate.now())
                .build();

        Certificate savedCertificate = persistenceService.save(certificate);
        Certificate result = assertDoesNotThrow(() -> persistenceService
                .findLeadershipExperience(savedCertificate.getId()));

        assertEquals(savedCertificate.getId(), result.getId());
    }
}
