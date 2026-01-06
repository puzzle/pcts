package ch.puzzle.pcts.service.persistence;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.CertificateRepository;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import ch.puzzle.pcts.repository.MemberRepository;
import ch.puzzle.pcts.repository.OrganisationUnitRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CertificatePersistenceServiceIT
        extends
            PersistenceBaseIT<Certificate, CertificateRepository, CertificatePersistenceService> {

    @Autowired
    CertificatePersistenceServiceIT(CertificatePersistenceService service) {
        super(service);
    }

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CertificateTypeRepository certificateTypeRepository;

    @Autowired
    private OrganisationUnitRepository organisationUnitRepository;

    @Override
    Certificate getModel() {
        return createCertificate(null,
                                 createMember(1L,
                                              "Member 1",
                                              "M1",
                                              LocalDate.of(2021, 7, 15),
                                              LocalDate.of(1999, 8, 10),
                                              createOrganisationUnit(1L,
                                                                     "OrganisationUnit 1",
                                                                     LocalDateTime
                                                                             .ofInstant(Instant.EPOCH,
                                                                                        ZoneOffset.UTC))),
                                 createCertificateType(1L,
                                                       "Certificate Type 1",
                                                       new BigDecimal("5.5"),
                                                       "This is Certificate 1",
                                                       Set.of(new Tag(1L, "Tag 1"))),
                                 LocalDate.of(2021, 7, 15),
                                 LocalDate.of(2021, 7, 15),
                                 "Comment");
    }

    @Override
    List<Certificate> getAll() {
        Certificate certificate1 = createCertificate(1L,
                                                     createMember(1L,
                                                                  "Member 1",
                                                                  "M1",
                                                                  LocalDate.of(2021, 7, 15),
                                                                  LocalDate.of(1999, 8, 10),
                                                                  createOrganisationUnit(1L,
                                                                                         "OrganisationUnit 1",
                                                                                         LocalDateTime
                                                                                                 .of(1970,
                                                                                                     1,
                                                                                                     1,
                                                                                                     0,
                                                                                                     0))),
                                                     createCertificateType(1L,
                                                                           "Certificate Type 1",
                                                                           new BigDecimal("5.5"),
                                                                           "This is Certificate 1",
                                                                           Set.of(new Tag(1L, "Tag 1"))),
                                                     LocalDate.of(2023, 1, 15),
                                                     LocalDate.of(2025, 1, 14),
                                                     "Completed first aid training.");
        Certificate certificate2 = createCertificate(2L,
                                                     createMember(2L,
                                                                  "Member 2",
                                                                  "M2",
                                                                  LocalDate.of(2020, 6, 1),
                                                                  LocalDate.of(1998, 3, 3),
                                                                  createOrganisationUnit(2L,
                                                                                         "OrganisationUnit 2",
                                                                                         null)),
                                                     createCertificateType(2L,
                                                                           "Certificate Type 2",
                                                                           new BigDecimal("1"),
                                                                           "This is Certificate 2",
                                                                           Set.of(new Tag(2L, "Longer tag name"))),
                                                     LocalDate.of(2022, 11, 1),
                                                     null,
                                                     "Completed first aid training.");
        Certificate certificate3 = createCertificate(3L,
                                                     createMember(2L,
                                                                  "Member 2",
                                                                  "M2",
                                                                  LocalDate.of(2020, 6, 1),
                                                                  LocalDate.of(1998, 3, 3),
                                                                  createOrganisationUnit(2L,
                                                                                         "OrganisationUnit 2",
                                                                                         null)),
                                                     createCertificateType(1L,
                                                                           "Certificate Type 1",
                                                                           new BigDecimal("5.5"),
                                                                           "This is Certificate 1",
                                                                           Set.of(new Tag(1L, "Tag 1"))),
                                                     LocalDate.of(2023, 1, 15),
                                                     LocalDate.of(2025, 1, 14),
                                                     null);
        Certificate certificate4 = createCertificate(4L,
                                                     createMember(1L,
                                                                  "Member 1",
                                                                  "M1",
                                                                  LocalDate.of(2021, 7, 15),
                                                                  LocalDate.of(1999, 8, 10),
                                                                  createOrganisationUnit(1L,
                                                                                         "OrganisationUnit 1",
                                                                                         LocalDateTime
                                                                                                 .of(1970,
                                                                                                     1,
                                                                                                     1,
                                                                                                     0,
                                                                                                     0))),
                                                     createCertificateType(2L,
                                                                           "Certificate Type 2",
                                                                           new BigDecimal("1"),
                                                                           "This is Certificate 2",
                                                                           Set.of(new Tag(2L, "Longer tag name"))),
                                                     LocalDate.of(2010, 8, 12),
                                                     LocalDate.of(2023, 3, 25),
                                                     "Left organization.");

        return List.of(certificate1, certificate2, certificate3, certificate4);
    }

    private Member createMember(Long id, String firstName, String abbreviation, LocalDate dateOfHire,
                                LocalDate birthDate, OrganisationUnit organisationUnit) {
        return Member.Builder
                .builder()
                .withId(id)
                .withFirstName(firstName)
                .withLastName("Test")
                .withEmploymentState(EmploymentState.MEMBER)
                .withAbbreviation(abbreviation)
                .withDateOfHire(dateOfHire)
                .withBirthDate(birthDate)
                .withOrganisationUnit(organisationUnit)
                .build();
    }

    private OrganisationUnit createOrganisationUnit(Long id, String name, LocalDateTime deletedAt) {
        OrganisationUnit organisationUnit = new OrganisationUnit(id, name);
        organisationUnit.setDeletedAt(deletedAt);
        return organisationUnit;
    }

    private CertificateType createCertificateType(Long id, String name, BigDecimal points, String comment,
                                                  Set<Tag> tags) {
        CertificateType certificateType = new CertificateType();
        certificateType.setId(id);
        certificateType.setName(name);
        certificateType.setPoints(points);
        certificateType.setComment(comment);
        certificateType.setTags(tags);
        certificateType.setCertificateKind(CertificateKind.CERTIFICATE);
        certificateType.setDeletedAt(null);
        return certificateType;
    }

    private Certificate createCertificate(Long id, Member member, CertificateType certificateType,
                                          LocalDate completedAt, LocalDate validUntil, String comment) {
        return Certificate.Builder
                .builder()
                .withId(id)
                .withMember(member)
                .withCertificateType(certificateType)
                .withCompletedAt(completedAt)
                .withValidUntil(validUntil)
                .withComment(comment)
                .build();
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
