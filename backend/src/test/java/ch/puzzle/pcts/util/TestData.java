package ch.puzzle.pcts.util;

import ch.puzzle.pcts.dto.memberoverview.MemberCvDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewMemberDto;
import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateDto;
import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateTypeDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeTypeDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceTypeDto;
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
import ch.puzzle.pcts.model.memberoverview.MemberOverview;
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
    private static final Experience EXPERIENCE_1;
    static {
        EXPERIENCE_1 = new Experience.Builder()
                .withId(1L)
                .withMember(MEMBER_1)
                .withName("Experience 1")
                .withEmployer("Employer 1")
                .withPercent(100)
                .withType(EXP_TYPE_1)
                .withComment("Comment test 1")
                .withStartDate(LocalDate.of(2021, 7, 15))
                .withEndDate(LocalDate.of(2022, 7, 15))
                .build();
        EXPERIENCE_1.setDeletedAt(UNIX_EPOCH);
    }

    public static final Experience EXPERIENCE_2 = new Experience.Builder()
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

    public static final Experience EXPERIENCE_3 = new Experience.Builder()
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

    public static final List<MemberOverview> MEMBER_1_OVERVIEWS = List.of(
            MemberOverview.Builder.builder()
                    .withUniqueRowId(1L)
                    .withMemberId(TestData.MEMBER_1.getId())
                    .withFirstName(TestData.MEMBER_1.getFirstName())
                    .withLastName(TestData.MEMBER_1.getLastName())
                    .withAbbreviation(TestData.MEMBER_1.getAbbreviation())
                    .withEmploymentState(TestData.MEMBER_1.getEmploymentState())
                    .withDateOfHire(TestData.MEMBER_1.getDateOfHire())
                    .withBirthDate(TestData.MEMBER_1.getBirthDate())
                    .withOrganisationUnitName(TestData.MEMBER_1.getOrganisationUnit().getName())
                    .withCertificateId(TestData.CERTIFICATE_1.getId())
                    .withCertificateCompletedAt(TestData.CERTIFICATE_1.getCompletedAt())
                    .withCertificateValidUntil(TestData.CERTIFICATE_1.getValidUntil())
                    .withCertificateComment(TestData.CERTIFICATE_1.getComment())
                    .withCertificateTypeName(TestData.CERTIFICATE_1.getCertificateType().getName())
                    .withCertificateTypeComment(TestData.CERTIFICATE_1.getCertificateType().getComment())
                    .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                    .withDegreeId(TestData.DEGREE_1.getId())
                    .withDegreeName(TestData.DEGREE_1.getName())
                    .withDegreeStartDate(TestData.DEGREE_1.getStartDate())
                    .withDegreeEndDate(TestData.DEGREE_1.getEndDate())
                    .withDegreeComment(TestData.DEGREE_1.getComment())
                    .withDegreeTypeName(TestData.DEGREE_1.getDegreeType().getName())
                    .withExperienceId(TestData.EXPERIENCE_1.getId())
                    .withExperienceName(TestData.EXPERIENCE_1.getName())
                    .withExperienceEmployer(TestData.EXPERIENCE_1.getEmployer())
                    .withExperienceStartDate(TestData.EXPERIENCE_1.getStartDate())
                    .withExperienceEndDate(TestData.EXPERIENCE_1.getEndDate())
                    .withExperienceComment(TestData.EXPERIENCE_1.getComment())
                    .withExperienceTypeName(TestData.EXPERIENCE_1.getType().getName())
                    .build(),

            MemberOverview.Builder.builder()
                    .withUniqueRowId(2L)
                    .withMemberId(TestData.MEMBER_1.getId())
                    .withFirstName(TestData.MEMBER_1.getFirstName())
                    .withLastName(TestData.MEMBER_1.getLastName())
                    .withAbbreviation(TestData.MEMBER_1.getAbbreviation())
                    .withEmploymentState(TestData.MEMBER_1.getEmploymentState())
                    .withDateOfHire(TestData.MEMBER_1.getDateOfHire())
                    .withBirthDate(TestData.MEMBER_1.getBirthDate())
                    .withOrganisationUnitName(TestData.MEMBER_1.getOrganisationUnit().getName())
                    .withCertificateId(TestData.CERTIFICATE_1.getId())
                    .withCertificateCompletedAt(TestData.CERTIFICATE_1.getCompletedAt())
                    .withCertificateValidUntil(TestData.CERTIFICATE_1.getValidUntil())
                    .withCertificateComment(TestData.CERTIFICATE_1.getComment())
                    .withCertificateTypeName(TestData.CERTIFICATE_1.getCertificateType().getName())
                    .withCertificateTypeComment(TestData.CERTIFICATE_1.getCertificateType().getComment())
                    .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                    .withDegreeId(TestData.DEGREE_1.getId())
                    .withDegreeName(TestData.DEGREE_1.getName())
                    .withDegreeStartDate(TestData.DEGREE_1.getStartDate())
                    .withDegreeEndDate(TestData.DEGREE_1.getEndDate())
                    .withDegreeComment(TestData.DEGREE_1.getComment())
                    .withDegreeTypeName(TestData.DEGREE_1.getDegreeType().getName())
                    .withExperienceId(TestData.EXPERIENCE_2.getId())
                    .withExperienceName(TestData.EXPERIENCE_2.getName())
                    .withExperienceEmployer(TestData.EXPERIENCE_2.getEmployer())
                    .withExperienceStartDate(TestData.EXPERIENCE_2.getStartDate())
                    .withExperienceEndDate(TestData.EXPERIENCE_2.getEndDate())
                    .withExperienceComment(TestData.EXPERIENCE_2.getComment())
                    .withExperienceTypeName(TestData.EXPERIENCE_2.getType().getName())
                    .build(),

            MemberOverview.Builder.builder()
                    .withUniqueRowId(3L)
                    .withMemberId(TestData.MEMBER_1.getId())
                    .withFirstName(TestData.MEMBER_1.getFirstName())
                    .withLastName(TestData.MEMBER_1.getLastName())
                    .withAbbreviation(TestData.MEMBER_1.getAbbreviation())
                    .withEmploymentState(TestData.MEMBER_1.getEmploymentState())
                    .withDateOfHire(TestData.MEMBER_1.getDateOfHire())
                    .withBirthDate(TestData.MEMBER_1.getBirthDate())
                    .withOrganisationUnitName(TestData.MEMBER_1.getOrganisationUnit().getName())
                    .withCertificateId(TestData.CERTIFICATE_4.getId())
                    .withCertificateCompletedAt(TestData.CERTIFICATE_4.getCompletedAt())
                    .withCertificateValidUntil(TestData.CERTIFICATE_4.getValidUntil())
                    .withCertificateComment(TestData.CERTIFICATE_4.getComment())
                    .withCertificateTypeName(TestData.CERTIFICATE_4.getCertificateType().getName())
                    .withCertificateTypeComment(TestData.CERTIFICATE_4.getCertificateType().getComment())
                    .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                    .withDegreeId(TestData.DEGREE_1.getId())
                    .withDegreeName(TestData.DEGREE_1.getName())
                    .withDegreeStartDate(TestData.DEGREE_1.getStartDate())
                    .withDegreeEndDate(TestData.DEGREE_1.getEndDate())
                    .withDegreeComment(TestData.DEGREE_1.getComment())
                    .withDegreeTypeName(TestData.DEGREE_1.getDegreeType().getName())
                    .withExperienceId(TestData.EXPERIENCE_1.getId())
                    .withExperienceName(TestData.EXPERIENCE_1.getName())
                    .withExperienceEmployer(TestData.EXPERIENCE_1.getEmployer())
                    .withExperienceStartDate(TestData.EXPERIENCE_1.getStartDate())
                    .withExperienceEndDate(TestData.EXPERIENCE_1.getEndDate())
                    .withExperienceComment(TestData.EXPERIENCE_1.getComment())
                    .withExperienceTypeName(TestData.EXPERIENCE_1.getType().getName())
                    .build(),

            MemberOverview.Builder.builder()
                    .withUniqueRowId(4L)
                    .withMemberId(TestData.MEMBER_1.getId())
                    .withFirstName(TestData.MEMBER_1.getFirstName())
                    .withLastName(TestData.MEMBER_1.getLastName())
                    .withAbbreviation(TestData.MEMBER_1.getAbbreviation())
                    .withEmploymentState(TestData.MEMBER_1.getEmploymentState())
                    .withDateOfHire(TestData.MEMBER_1.getDateOfHire())
                    .withBirthDate(TestData.MEMBER_1.getBirthDate())
                    .withOrganisationUnitName(TestData.MEMBER_1.getOrganisationUnit().getName())
                    .withCertificateId(TestData.CERTIFICATE_4.getId())
                    .withCertificateCompletedAt(TestData.CERTIFICATE_4.getCompletedAt())
                    .withCertificateValidUntil(TestData.CERTIFICATE_4.getValidUntil())
                    .withCertificateComment(TestData.CERTIFICATE_4.getComment())
                    .withCertificateTypeName(TestData.CERTIFICATE_4.getCertificateType().getName())
                    .withCertificateTypeComment(TestData.CERTIFICATE_4.getCertificateType().getComment())
                    .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                    .withDegreeId(TestData.DEGREE_1.getId())
                    .withDegreeName(TestData.DEGREE_1.getName())
                    .withDegreeStartDate(TestData.DEGREE_1.getStartDate())
                    .withDegreeEndDate(TestData.DEGREE_1.getEndDate())
                    .withDegreeComment(TestData.DEGREE_1.getComment())
                    .withDegreeTypeName(TestData.DEGREE_1.getDegreeType().getName())
                    .withExperienceId(TestData.EXPERIENCE_2.getId())
                    .withExperienceName(TestData.EXPERIENCE_2.getName())
                    .withExperienceEmployer(TestData.EXPERIENCE_2.getEmployer())
                    .withExperienceStartDate(TestData.EXPERIENCE_2.getStartDate())
                    .withExperienceEndDate(TestData.EXPERIENCE_2.getEndDate())
                    .withExperienceComment(TestData.EXPERIENCE_2.getComment())
                    .withExperienceTypeName(TestData.EXPERIENCE_2.getType().getName())
                    .build()
    );

    public static final MemberOverviewDto MEMBER_1_OVERVIEW_DTO = new MemberOverviewDto(
            new MemberOverviewMemberDto(
                    TestData.MEMBER_1.getId(),
                    TestData.MEMBER_1.getFirstName(),
                    TestData.MEMBER_1.getLastName(),
                    TestData.MEMBER_1.getEmploymentState(),
                    TestData.MEMBER_1.getAbbreviation(),
                    TestData.MEMBER_1.getDateOfHire(),
                    TestData.MEMBER_1.getBirthDate(),
                    TestData.MEMBER_1.getOrganisationUnit().getName()
            ),
            new MemberCvDto(
                    List.of(
                            new MemberOverviewDegreeDto(
                                    TestData.DEGREE_1.getId(),
                                    TestData.DEGREE_1.getName(),
                                    new MemberOverviewDegreeTypeDto(TestData.DEGREE_1.getDegreeType().getName()),
                                    TestData.DEGREE_1.getStartDate(),
                                    TestData.DEGREE_1.getEndDate(),
                                    TestData.DEGREE_1.getComment()
                            )
                    ),
                    List.of(
                            new MemberOverviewExperienceDto(
                                    TestData.EXPERIENCE_1.getId(),
                                    TestData.EXPERIENCE_1.getName(),
                                    TestData.EXPERIENCE_1.getEmployer(),
                                    new MemberOverviewExperienceTypeDto(TestData.EXPERIENCE_1.getType().getName()),
                                    TestData.EXPERIENCE_1.getComment(),
                                    TestData.EXPERIENCE_1.getStartDate(),
                                    TestData.EXPERIENCE_1.getEndDate()
                            ),
                            new MemberOverviewExperienceDto(
                                    TestData.EXPERIENCE_2.getId(),
                                    TestData.EXPERIENCE_2.getName(),
                                    TestData.EXPERIENCE_2.getEmployer(),
                                    new MemberOverviewExperienceTypeDto(TestData.EXPERIENCE_2.getType().getName()),
                                    TestData.EXPERIENCE_2.getComment(),
                                    TestData.EXPERIENCE_2.getStartDate(),
                                    TestData.EXPERIENCE_2.getEndDate()
                            )
                    ),
                    List.of(
                            new MemberOverviewCertificateDto(
                                    TestData.CERTIFICATE_1.getId(),
                                    new MemberOverviewCertificateTypeDto(
                                            TestData.CERTIFICATE_1.getCertificateType().getName(),
                                            TestData.CERTIFICATE_1.getCertificateType().getComment()
                                    ),
                                    TestData.CERTIFICATE_1.getCompletedAt(),
                                    TestData.CERTIFICATE_1.getValidUntil(),
                                    TestData.CERTIFICATE_1.getComment()
                            ),
                            new MemberOverviewCertificateDto(
                                    TestData.CERTIFICATE_4.getId(),
                                    new MemberOverviewCertificateTypeDto(
                                            TestData.CERTIFICATE_4.getCertificateType().getName(),
                                            TestData.CERTIFICATE_4.getCertificateType().getComment()
                                    ),
                                    TestData.CERTIFICATE_4.getCompletedAt(),
                                    TestData.CERTIFICATE_4.getValidUntil(),
                                    TestData.CERTIFICATE_4.getComment()
                            )
                    ),
                    List.of()
            )
    );

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

    public static final List<Experience> EXPERIENCES = List.of(EXPERIENCE_2, EXPERIENCE_3);

    public static final List<Calculation> CALCULATIONS = List.of(CALCULATION_1, CALCULATION_2, CALCULATION_3);

}
