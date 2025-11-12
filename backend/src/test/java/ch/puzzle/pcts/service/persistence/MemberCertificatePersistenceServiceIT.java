package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificate.CertificateType;
import ch.puzzle.pcts.model.certificate.Tag;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.membercertificate.MemberCertificate;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.repository.MemberCertificateRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberCertificatePersistenceServiceIT
        extends
            PersistenceBaseIT<MemberCertificate, MemberCertificateRepository, MemberCertificatePersistenceService> {

    @Autowired
    MemberCertificatePersistenceServiceIT(MemberCertificatePersistenceService service) {
        super(service);
    }

    /**
     * Returns an object of the entity used to save it in the database
     */
    @Override
    MemberCertificate getModel() {
        return createMemberCertificate(null,
                                       createMember(1L,
                                                    "Member 1",
                                                    "M1",
                                                    Timestamp.valueOf("2021-07-15 00:00:00"),
                                                    Timestamp.valueOf("1999-08-10 00:00:00"),
                                                    createOrganisationUnit(1L,
                                                                           "OrganisationUnit 1",
                                                                           new Timestamp(0L))),
                                       createCertificate(1L,
                                                         "Certificate 1",
                                                         new BigDecimal("5.5"),
                                                         "This is Certificate 1",
                                                         Set.of(new Tag(1L, "Tag 1"))),
                                       Timestamp.valueOf("2024-09-15 00:00:00"),
                                       Timestamp.valueOf("2023-04-15 00:00:00"),
                                       "Comment");
    }

    /**
     * A list of all the objects with this datatype stored in the database shouldn't
     * contain soft deleted ones
     */
    @Override
    List<MemberCertificate> getAll() {
        MemberCertificate memberCertificate1 = createMemberCertificate(1L,
                                                                       createMember(1L,
                                                                                    "Member 1",
                                                                                    "M1",
                                                                                    Timestamp
                                                                                            .valueOf("2021-07-15 00:00:00"),
                                                                                    Timestamp
                                                                                            .valueOf("1999-08-10 00:00:00"),
                                                                                    createOrganisationUnit(1L,
                                                                                                           "OrganisationUnit 1",
                                                                                                           new Timestamp(0L))),
                                                                       createCertificate(1L,
                                                                                         "Certificate 1",
                                                                                         new BigDecimal("5.5"),
                                                                                         "This is Certificate 1",
                                                                                         Set.of(new Tag(1L, "Tag 1"))),
                                                                       Timestamp.valueOf("2023-01-15 00:00:00"),
                                                                       Timestamp.valueOf("2025-01-14 00:00:00"),
                                                                       "Completed first aid training.");

        MemberCertificate memberCertificate2 = createMemberCertificate(2L,
                                                                       createMember(2L,
                                                                                    "Member 2",
                                                                                    "M2",
                                                                                    Timestamp
                                                                                            .valueOf("2020-06-01 00:00:00"),
                                                                                    Timestamp
                                                                                            .valueOf("1998-03-03 00:00:00"),
                                                                                    createOrganisationUnit(2L,
                                                                                                           "OrganisationUnit 2",
                                                                                                           null)),
                                                                       createCertificate(2L,
                                                                                         "Certificate 2",
                                                                                         new BigDecimal("1"),
                                                                                         "This is Certificate 2",
                                                                                         Set
                                                                                                 .of(new Tag(2L,
                                                                                                             "Longer tag name"))),
                                                                       Timestamp.valueOf("2022-11-01 00:00:00"),
                                                                       null,
                                                                       "Lifetime certification.");

        return List.of(memberCertificate1, memberCertificate2);
    }

    private Member createMember(Long id, String firstName, String abbreviation, Date dateOfHire, Date birthDate,
                                OrganisationUnit organisationUnit) {
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

    private OrganisationUnit createOrganisationUnit(Long id, String name, Date deletedAt) {
        OrganisationUnit organisationUnit = new OrganisationUnit(id, name);
        if (deletedAt != null) {
            organisationUnit.setDeletedAt(new Timestamp(0L));
        }
        return organisationUnit;
    }

    private Certificate createCertificate(Long id, String name, BigDecimal points, String comment, Set<Tag> tags) {
        Certificate certificate = new Certificate();
        certificate.setId(id);
        certificate.setName(name);
        certificate.setPoints(points);
        certificate.setComment(comment);
        certificate.setTags(tags);
        certificate.setCertificateType(CertificateType.CERTIFICATE);

        return certificate;
    }

    private MemberCertificate createMemberCertificate(Long id, Member member, Certificate certificate, Date completedAt,
                                                      Date validUntil, String comment) {
        return MemberCertificate.Builder
                .builder()
                .withId(id)
                .withMember(member)
                .withCertificate(certificate)
                .withCompleted_at(completedAt)
                .withValid_until(validUntil)
                .withComment(comment)
                .build();
    }
}
