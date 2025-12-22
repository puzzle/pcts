package ch.puzzle.pcts.util;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.EmploymentState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.model.role.Role;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TestData {

    private static final LocalDateTime UNIX_EPOCH = LocalDateTime.of(1970, 1, 1, 0, 0);

    private TestData() {
    }

    public static final OrganisationUnit ORG_UNIT_1;
    static {
        ORG_UNIT_1 = new OrganisationUnit(1L, "OrganisationUnit 1");
        ORG_UNIT_1.setDeletedAt(UNIX_EPOCH);
    }

    public static final OrganisationUnit ORG_UNIT_2 = new OrganisationUnit(2L, "OrganisationUnit 2");

    public static final Role ROLE_2 = new Role(2L, "Role 2", false);

    public static final Tag TAG_1 = new Tag(1L, "Tag 1");
    public static final Tag TAG_2 = new Tag(2L, "Longer tag name");

    public static final Member MEMBER_1 = Member.Builder
            .builder()
            .withId(1L)
            .withFirstName("Member 1")
            .withLastName("Test")
            .withEmploymentState(EmploymentState.MEMBER)
            .withAbbreviation("M1")
            .withDateOfHire(LocalDate.of(2021, 7, 15))
            .withBirthDate(LocalDate.of(1999, 8, 10))
            .withOrganisationUnit(ORG_UNIT_1)
            .build();

    public static final Member MEMBER_2 = Member.Builder
            .builder()
            .withId(2L)
            .withFirstName("Member 2")
            .withLastName("Test")
            .withEmploymentState(EmploymentState.MEMBER)
            .withAbbreviation("M2")
            .withDateOfHire(LocalDate.of(2020, 6, 1))
            .withBirthDate(LocalDate.of(1998, 3, 3))
            .withOrganisationUnit(ORG_UNIT_2)
            .build();

    public static final CertificateType CERT_TYPE_1 = new CertificateType(1L,
                                                                          "Certificate Type 1",
                                                                          BigDecimal.valueOf(5.5),
                                                                          "This is Certificate 1",
                                                                          Set.of(TAG_1),
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType CERT_TYPE_2 = new CertificateType(2L,
                                                                          "Certificate Type 2",
                                                                          BigDecimal.valueOf(1),
                                                                          "This is Certificate 2",
                                                                          Set.of(TAG_2),
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType CERT_TYPE_3 = new CertificateType(3L,
                                                                          "Certificate Type 3",
                                                                          BigDecimal.valueOf(3),
                                                                          "This is Certificate 3",
                                                                          Set.of(),
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType CERT_TYPE_4 = new CertificateType(4L,
                                                                          "Certificate Type 4",
                                                                          BigDecimal.valueOf(0.5),
                                                                          "This is Certificate 4",
                                                                          Set.of(),
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType LEADERSHIP_TYPE_1 = new CertificateType(5L,
                                                                                "LeadershipExperience Type 1",
                                                                                BigDecimal.valueOf(5.5),
                                                                                "This is LeadershipExperience 1",
                                                                                Set.of(),
                                                                                CertificateKind.MILITARY_FUNCTION);

    public static final CertificateType LEADERSHIP_TYPE_2 = new CertificateType(6L,
                                                                                "LeadershipExperience Type 2",
                                                                                BigDecimal.valueOf(1),
                                                                                "This is LeadershipExperience 2",
                                                                                Set.of(),
                                                                                CertificateKind.YOUTH_AND_SPORT);

    public static final CertificateType LEADERSHIP_TYPE_3 = new CertificateType(7L,
                                                                                "LeadershipExperience Type 3",
                                                                                BigDecimal.valueOf(3),
                                                                                "This is LeadershipExperience 3",
                                                                                Set.of(),
                                                                                CertificateKind.LEADERSHIP_TRAINING);

    public static final Certificate CERTIFICATE_1 = Certificate.Builder
            .builder()
            .withId(1L)
            .withMember(MEMBER_1)
            .withCertificateType(CERT_TYPE_1)
            .withCompletedAt(LocalDate.of(2023, 1, 15))
            .withValidUntil(LocalDate.of(2025, 1, 14))
            .withComment("Completed first aid training.")
            .build();

    public static final Certificate CERTIFICATE_2 = Certificate.Builder
            .builder()
            .withId(2L)
            .withMember(MEMBER_2)
            .withCertificateType(CERT_TYPE_2)
            .withCompletedAt(LocalDate.of(2022, 11, 1))
            .withValidUntil(null)
            .withComment("Completed first aid training.")
            .build();

    public static final Certificate CERTIFICATE_3 = Certificate.Builder
            .builder()
            .withId(3L)
            .withMember(MEMBER_2)
            .withCertificateType(CERT_TYPE_1)
            .withCompletedAt(LocalDate.of(2023, 1, 15))
            .withValidUntil(LocalDate.of(2025, 1, 14))
            .withComment(null)
            .build();

    public static final Certificate CERTIFICATE_4 = Certificate.Builder
            .builder()
            .withId(4L)
            .withMember(MEMBER_1)
            .withCertificateType(CERT_TYPE_2)
            .withCompletedAt(LocalDate.of(2010, 8, 12))
            .withValidUntil(LocalDate.of(2023, 3, 25))
            .withComment("Left organization.")
            .build();

    public static final DegreeType DEGREE_TYPE_1 = new DegreeType(1L,
                                                                  "Degree type 1",
                                                                  BigDecimal.valueOf(120.55),
                                                                  BigDecimal.valueOf(60),
                                                                  BigDecimal.valueOf(15.45));

    public static final DegreeType DEGREE_TYPE_2 = new DegreeType(2L,
                                                                  "Degree type 2",
                                                                  BigDecimal.valueOf(12),
                                                                  BigDecimal.valueOf(3.961),
                                                                  BigDecimal.valueOf(3));

    public static final Degree DEGREE_1 = Degree.Builder
            .builder()
            .withId(1L)
            .withMember(MEMBER_1)
            .withName("Degree 1")
            .withInstitution("Institution")
            .withCompleted(true)
            .withDegreeType(DEGREE_TYPE_1)
            .withStartDate(LocalDate.of(2015, 9, 1))
            .withEndDate(LocalDate.of(2020, 6, 1))
            .withComment("Comment")
            .build();

    public static final Degree DEGREE_2 = Degree.Builder
            .builder()
            .withId(2L)
            .withMember(MEMBER_2)
            .withName("Degree 2")
            .withInstitution("Institution")
            .withCompleted(false)
            .withDegreeType(DEGREE_TYPE_2)
            .withStartDate(LocalDate.of(2016, 9, 1))
            .withEndDate(LocalDate.of(2019, 6, 30))
            .withComment("Comment")
            .build();

    public static final ExperienceType EXP_TYPE_1 = new ExperienceType(1L,
                                                                       "ExperienceType 1",
                                                                       BigDecimal.ZERO,
                                                                       BigDecimal.valueOf(12),
                                                                       BigDecimal.valueOf(4.005));

    public static final ExperienceType EXP_TYPE_2 = new ExperienceType(2L,
                                                                       "ExperienceType 2",
                                                                       BigDecimal.valueOf(12),
                                                                       BigDecimal.valueOf(10.7989),
                                                                       BigDecimal.valueOf(6));

    public static final Experience EXPERIENCE_1 = new Experience.Builder()
            .withId(2L)
            .withMember(MEMBER_1)
            .withName("Experience 2")
            .withEmployer("Employer 2")
            .withPercent(80)
            .withType(EXP_TYPE_2)
            .withComment("Comment test 2")
            .withStartDate(LocalDate.of(2022, 7, 16))
            .withEndDate(LocalDate.of(2023, 7, 15))
            .build();

    public static final Experience EXPERIENCE_2 = new Experience.Builder()
            .withId(3L)
            .withMember(MEMBER_2)
            .withName("Experience 3")
            .withEmployer("Employer 3")
            .withPercent(60)
            .withType(EXP_TYPE_1)
            .withComment("Comment test 3")
            .withStartDate(LocalDate.of(2023, 7, 16))
            .withEndDate(LocalDate.of(2024, 7, 15))
            .build();

    public static final Calculation CALCULATION_1 = new Calculation(1L,
                                                                    MEMBER_1,
                                                                    ROLE_2,
                                                                    CalculationState.DRAFT,
                                                                    LocalDate.of(2025, 1, 14),
                                                                    "Ldap User");

    public static final Calculation CALCULATION_2 = new Calculation(2L,
                                                                    MEMBER_2,
                                                                    ROLE_2,
                                                                    CalculationState.ARCHIVED,
                                                                    LocalDate.of(2025, 1, 14),
                                                                    "Ldap User 2");

    public static final Calculation CALCULATION_3 = new Calculation(3L,
                                                                    MEMBER_2,
                                                                    ROLE_2,
                                                                    CalculationState.ACTIVE,
                                                                    null,
                                                                    null);

    public static final List<OrganisationUnit> ORGANISATION_UNITS = List.of(ORG_UNIT_2);

    public static final List<Role> ROLES = List.of(ROLE_2);

    public static final List<Tag> TAGS = List.of(TAG_1, TAG_2);

    public static final List<Member> MEMBERS = List.of(MEMBER_1, MEMBER_2);

    public static final List<CertificateType> CERTIFICATE_TYPES = List
            .of(CERT_TYPE_1, CERT_TYPE_2, CERT_TYPE_3, CERT_TYPE_4);

    public static final List<CertificateType> LEADERSHIP_EXPERIENCE_TYPES = List
            .of(LEADERSHIP_TYPE_1, LEADERSHIP_TYPE_2, LEADERSHIP_TYPE_3);

    public static final List<Certificate> CERTIFICATES = List
            .of(CERTIFICATE_1, CERTIFICATE_2, CERTIFICATE_3, CERTIFICATE_4);

    public static final List<DegreeType> DEGREE_TYPES = List.of(DEGREE_TYPE_1, DEGREE_TYPE_2);

    public static final List<Degree> DEGREES = List.of(DEGREE_1, DEGREE_2);

    public static final List<ExperienceType> EXPERIENCE_TYPES = List.of(EXP_TYPE_1, EXP_TYPE_2);

    public static final List<Experience> EXPERIENCES = List.of(EXPERIENCE_1, EXPERIENCE_2);

    public static final List<Calculation> CALCULATIONS = List.of(CALCULATION_1, CALCULATION_2, CALCULATION_3);

}
