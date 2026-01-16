package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.CertificateRepository;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import ch.puzzle.pcts.repository.MemberRepository;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
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
        return Certificate.Builder
                .builder()
                .withId(null)
                .withMember(MEMBER_1)
                .withCompletedAt(LocalDate.of(2021, 7, 15))
                .withValidUntil(LocalDate.of(2021, 7, 15))
                .withCertificateType(CERT_TYPE_1)
                .build();
    }

    @Override
    List<Certificate> getAll() {
        return CERTIFICATES;
    }

    @Test
    @DisplayName("Should get leadership experience when kind is not CERTIFICATE")
    @Transactional
    void shouldGetLeadershipExperience() {
        OrganisationUnit organisationUnit = new OrganisationUnit();
        organisationUnit.setName("OrganisationUnit for testing");
        organisationUnit = organisationUnitRepository.save(organisationUnit);

        Member member = new Member();
        member.setFirstName("Member");
        member.setLastName("Test");
        member.setEmploymentState(EmploymentState.MEMBER);
        member.setOrganisationUnit(organisationUnit);
        member.setBirthDate(LocalDate.of(1999, 8, 10));
        member = memberRepository.save(member);

        CertificateType certificateType = new CertificateType();
        certificateType.setName("New certificatetype");
        certificateType.setCertificateKind(CertificateKind.MILITARY_FUNCTION);
        certificateType.setPoints(BigDecimal.valueOf(10));
        certificateType = certificateTypeRepository.save(certificateType); // Save and capture

        Certificate certificate = Certificate.Builder
                .builder()
                .withMember(member)
                .withCertificateType(certificateType)
                .withCompletedAt(LocalDate.now())
                .build();

        Certificate savedCertificate = persistenceService.save(certificate);
        Certificate result = assertDoesNotThrow(() -> persistenceService
                .findLeadershipExperience(savedCertificate.getId()));

        assertEquals(savedCertificate.getId(), result.getId());
    }
}
