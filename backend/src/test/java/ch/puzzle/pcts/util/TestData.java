package ch.puzzle.pcts.util;

import ch.puzzle.pcts.dto.calculation.CalculationDto;
import ch.puzzle.pcts.dto.calculation.CalculationInputDto;
import ch.puzzle.pcts.dto.certificate.CertificateDto;
import ch.puzzle.pcts.dto.certificate.CertificateInputDto;
import ch.puzzle.pcts.dto.certificatetype.CertificateTypeDto;
import ch.puzzle.pcts.dto.degree.DegreeDto;
import ch.puzzle.pcts.dto.degree.DegreeInputDto;
import ch.puzzle.pcts.dto.degreetype.DegreeTypeDto;
import ch.puzzle.pcts.dto.experience.ExperienceDto;
import ch.puzzle.pcts.dto.experience.ExperienceInputDto;
import ch.puzzle.pcts.dto.experiencetype.ExperienceTypeDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceDto;
import ch.puzzle.pcts.dto.leadershipexperience.LeadershipExperienceInputDto;
import ch.puzzle.pcts.dto.leadershipexperiencetype.LeadershipExperienceTypeDto;
import ch.puzzle.pcts.dto.member.MemberDto;
import ch.puzzle.pcts.dto.member.MemberInputDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewCvDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewDto;
import ch.puzzle.pcts.dto.memberoverview.MemberOverviewMemberDto;
import ch.puzzle.pcts.dto.memberoverview.certificate.MemberOverviewCertificateDto;
import ch.puzzle.pcts.dto.memberoverview.degree.MemberOverviewDegreeDto;
import ch.puzzle.pcts.dto.memberoverview.experience.MemberOverviewExperienceDto;
import ch.puzzle.pcts.dto.organisationunit.OrganisationUnitDto;
import ch.puzzle.pcts.dto.role.RoleDto;
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

    public static final LocalDate DATE_NOW = LocalDate.now();
    public static final LocalDate DATE_YESTERDAY = DATE_NOW.minusDays(1);
    public static final LocalDate DATE_TOMORROW = DATE_NOW.plusDays(1);

    public static final BigDecimal POSITIVE_BIG_DECIMAL = BigDecimal.valueOf(1);
    public static final BigDecimal NEGATIVE_BIG_DECIMAL = BigDecimal.valueOf(-1);

    public static final String VALID_STRING = "Valid String";
    public static final String TOO_LONG_STRING = "a".repeat(251);

    public static final Long INVALID_ID = 999L;

    public static final Long GENERIC_1_ID = 1L;
    public static final Long GENERIC_2_ID = 2L;

    public static final Long ORG_UNIT_1_ID = 1L;
    public static final Long ORG_UNIT_2_ID = 2L;

    public static final Long ROLE_1_ID = 1L;
    public static final Long ROLE_2_ID = 2L;

    public static final Long TAG_1_ID = 1L;
    public static final Long TAG_2_ID = 2L;

    public static final Long MEMBER_1_ID = 1L;
    public static final Long MEMBER_2_ID = 2L;

    public static final Long CERT_TYPE_1_ID = 1L;
    public static final Long CERT_TYPE_2_ID = 2L;
    public static final Long CERT_TYPE_3_ID = 3L;
    public static final Long CERT_TYPE_4_ID = 4L;
    public static final Long CERT_TYPE_5_ID = 5L;
    public static final Long CERT_TYPE_6_ID = 6L;

    public static final Long LEADERSHIP_TYPE_1_ID = 5L;
    public static final Long LEADERSHIP_TYPE_2_ID = 6L;
    public static final Long LEADERSHIP_TYPE_3_ID = 7L;
    public static final Long LEADERSHIP_TYPE_4_ID = 8L;
    public static final Long LEADERSHIP_TYPE_5_ID = 9L;

    public static final Long CERTIFICATE_1_ID = 1L;
    public static final Long CERTIFICATE_2_ID = 2L;
    public static final Long CERTIFICATE_3_ID = 3L;
    public static final Long CERTIFICATE_4_ID = 4L;

    public static final Long LEADERSHIP_CERT_1_ID = 1L;
    public static final Long LEADERSHIP_CERT_2_ID = 2L;

    public static final Long DEGREE_TYPE_1_ID = 1L;
    public static final Long DEGREE_TYPE_2_ID = 2L;

    public static final Long DEGREE_1_ID = 1L;
    public static final Long DEGREE_2_ID = 2L;

    public static final Long EXP_TYPE_1_ID = 1L;
    public static final Long EXP_TYPE_2_ID = 2L;

    public static final Long EXPERIENCE_1_ID = 1L;
    public static final Long EXPERIENCE_2_ID = 2L;
    public static final Long EXPERIENCE_3_ID = 3L;

    public static final Long CALCULATION_1_ID = 1L;
    public static final Long CALCULATION_2_ID = 2L;
    public static final Long CALCULATION_3_ID = 3L;

    public static final OrganisationUnit ORG_UNIT_1;
    static {
        ORG_UNIT_1 = new OrganisationUnit(ORG_UNIT_1_ID, "OrganisationUnit 1");
        ORG_UNIT_1.setDeletedAt(UNIX_EPOCH);
    }

    public static final OrganisationUnit ORG_UNIT_2 = new OrganisationUnit(ORG_UNIT_2_ID, "OrganisationUnit 2");

    public static final Role ROLE_2 = new Role(ROLE_2_ID, "Role 2", false);

    public static final Tag TAG_1 = new Tag(TAG_1_ID, "Tag 1");
    public static final Tag TAG_2 = new Tag(TAG_2_ID, "Longer tag name");
    public static final Tag TAG_3 = new Tag(null, "First tag without id");
    public static final Tag TAG_4 = new Tag(null, "Second tag without id");

    public static final Member MEMBER_1 = Member.Builder
            .builder()
            .withId(MEMBER_1_ID)
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
            .withId(MEMBER_2_ID)
            .withFirstName("Member 2")
            .withLastName("Test")
            .withEmploymentState(EmploymentState.MEMBER)
            .withAbbreviation("M2")
            .withDateOfHire(LocalDate.of(2020, 6, 1))
            .withBirthDate(LocalDate.of(1998, 3, 3))
            .withOrganisationUnit(ORG_UNIT_2)
            .build();

    public static final CertificateType CERT_TYPE_1 = new CertificateType(CERT_TYPE_1_ID,
                                                                          "Certificate Type 1",
                                                                          BigDecimal.valueOf(5.5),
                                                                          "This is Certificate 1",
                                                                          Set.of(TAG_1),
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType CERT_TYPE_2 = new CertificateType(CERT_TYPE_2_ID,
                                                                          "Certificate Type 2",
                                                                          BigDecimal.valueOf(1),
                                                                          "This is Certificate 2",
                                                                          Set.of(TAG_2),
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType CERT_TYPE_3 = new CertificateType(CERT_TYPE_3_ID,
                                                                          "Certificate Type 3",
                                                                          BigDecimal.valueOf(3),
                                                                          "This is Certificate 3",
                                                                          Set.of(),
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType CERT_TYPE_4 = new CertificateType(CERT_TYPE_4_ID,
                                                                          "Certificate Type 4",
                                                                          BigDecimal.valueOf(0.5),
                                                                          "This is Certificate 4",
                                                                          Set.of(),
                                                                          CertificateKind.CERTIFICATE);

    private static final Set<Tag> TAGS_2 = Set.of(TAG_3, TAG_4);

    public static final CertificateType CERT_TYPE_5 = new CertificateType(CERT_TYPE_5_ID,
                                                                          "Certificate Type 5",
                                                                          BigDecimal.valueOf(0.5),
                                                                          "This is Certificate 5",
                                                                          TAGS_2,
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType CERT_TYPE_6 = new CertificateType(CERT_TYPE_6_ID,
                                                                          "Certificate Type 6",
                                                                          BigDecimal.valueOf(0.5),
                                                                          "This is Certificate 6",
                                                                          TAGS_2,
                                                                          CertificateKind.CERTIFICATE);

    public static final CertificateType LEADERSHIP_TYPE_1 = new CertificateType(LEADERSHIP_TYPE_1_ID,
                                                                                "LeadershipExperience Type 1",
                                                                                BigDecimal.valueOf(5.5),
                                                                                "This is LeadershipExperience 1",
                                                                                Set.of(),
                                                                                CertificateKind.MILITARY_FUNCTION);

    public static final CertificateType LEADERSHIP_TYPE_2 = new CertificateType(LEADERSHIP_TYPE_2_ID,
                                                                                "LeadershipExperience Type 2",
                                                                                BigDecimal.valueOf(1),
                                                                                "This is LeadershipExperience 2",
                                                                                Set.of(),
                                                                                CertificateKind.YOUTH_AND_SPORT);

    public static final CertificateType LEADERSHIP_TYPE_3 = new CertificateType(LEADERSHIP_TYPE_3_ID,
                                                                                "LeadershipExperience Type 3",
                                                                                BigDecimal.valueOf(3),
                                                                                "This is LeadershipExperience 3",
                                                                                Set.of(),
                                                                                CertificateKind.LEADERSHIP_TRAINING);

    public static final CertificateType LEADERSHIP_TYPE_4 = new CertificateType(LEADERSHIP_TYPE_4_ID,
                                                                                "LeadershipExperience Type 4",
                                                                                BigDecimal.valueOf(4),
                                                                                "This is a comment.",
                                                                                null,
                                                                                CertificateKind.YOUTH_AND_SPORT);

    public static final CertificateType LEADERSHIP_TYPE_5 = new CertificateType(LEADERSHIP_TYPE_5_ID,
                                                                                "LeadershipExperience Type 5",
                                                                                BigDecimal.valueOf(2),
                                                                                "This is a comment.",
                                                                                null,
                                                                                CertificateKind.MILITARY_FUNCTION);

    public static final Certificate CERTIFICATE_1 = Certificate.Builder
            .builder()
            .withId(CERTIFICATE_1_ID)
            .withMember(MEMBER_1)
            .withCertificateType(CERT_TYPE_1)
            .withCompletedAt(LocalDate.of(2023, 1, 15))
            .withValidUntil(LocalDate.of(2025, 1, 14))
            .withComment("Completed first aid training.")
            .build();

    public static final Certificate CERTIFICATE_2 = Certificate.Builder
            .builder()
            .withId(CERTIFICATE_2_ID)
            .withMember(MEMBER_2)
            .withCertificateType(CERT_TYPE_2)
            .withCompletedAt(LocalDate.of(2022, 11, 1))
            .withValidUntil(null)
            .withComment("Completed first aid training.")
            .build();

    public static final Certificate CERTIFICATE_3 = Certificate.Builder
            .builder()
            .withId(CERTIFICATE_3_ID)
            .withMember(MEMBER_2)
            .withCertificateType(CERT_TYPE_1)
            .withCompletedAt(LocalDate.of(2023, 1, 15))
            .withValidUntil(LocalDate.of(2025, 1, 14))
            .withComment(null)
            .build();

    public static final Certificate CERTIFICATE_4 = Certificate.Builder
            .builder()
            .withId(CERTIFICATE_4_ID)
            .withMember(MEMBER_1)
            .withCertificateType(CERT_TYPE_2)
            .withCompletedAt(LocalDate.of(2010, 8, 12))
            .withValidUntil(LocalDate.of(2023, 3, 25))
            .withComment("Left organization.")
            .build();

    public static final Certificate LEADERSHIP_CERT_1 = Certificate.Builder
            .builder()
            .withId(LEADERSHIP_CERT_1_ID)
            .withMember(MEMBER_1)
            .withCertificateType(LEADERSHIP_TYPE_1)
            .withCompletedAt(DATE_NOW)
            .withValidUntil(DATE_NOW)
            .withComment("This is a comment.")
            .build();

    public static final Certificate LEADERSHIP_CERT_2 = Certificate.Builder
            .builder()
            .withId(LEADERSHIP_CERT_2_ID)
            .withMember(MEMBER_2)
            .withCertificateType(LEADERSHIP_TYPE_2)
            .withCompletedAt(DATE_NOW)
            .withValidUntil(DATE_NOW)
            .withComment("This is a comment.")
            .build();

    public static final DegreeType DEGREE_TYPE_1 = new DegreeType(DEGREE_TYPE_1_ID,
                                                                  "Degree type 1",
                                                                  BigDecimal.valueOf(120.55),
                                                                  BigDecimal.valueOf(60),
                                                                  BigDecimal.valueOf(15.45));

    public static final DegreeType DEGREE_TYPE_2 = new DegreeType(DEGREE_TYPE_2_ID,
                                                                  "Degree type 2",
                                                                  BigDecimal.valueOf(12),
                                                                  BigDecimal.valueOf(3.961),
                                                                  BigDecimal.valueOf(3));

    public static final Degree DEGREE_1 = Degree.Builder
            .builder()
            .withId(DEGREE_1_ID)
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
            .withId(DEGREE_2_ID)
            .withMember(MEMBER_2)
            .withName("Degree 2")
            .withInstitution("Institution")
            .withCompleted(false)
            .withDegreeType(DEGREE_TYPE_2)
            .withStartDate(LocalDate.of(2016, 9, 1))
            .withEndDate(LocalDate.of(2019, 6, 30))
            .withComment("Comment")
            .build();

    public static final ExperienceType EXP_TYPE_1 = new ExperienceType(EXP_TYPE_1_ID,
                                                                       "ExperienceType 1",
                                                                       BigDecimal.ZERO,
                                                                       BigDecimal.valueOf(12),
                                                                       BigDecimal.valueOf(4.005));

    public static final ExperienceType EXP_TYPE_2 = new ExperienceType(EXP_TYPE_2_ID,
                                                                       "ExperienceType 2",
                                                                       BigDecimal.valueOf(12),
                                                                       BigDecimal.valueOf(10.7989),
                                                                       BigDecimal.valueOf(6));
    public static final Experience EXPERIENCE_1;
    static {
        EXPERIENCE_1 = new Experience.Builder()
                .withId(EXPERIENCE_1_ID)
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
            .withId(EXPERIENCE_2_ID)
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
            .withId(EXPERIENCE_3_ID)
            .withMember(MEMBER_2)
            .withName("Experience 3")
            .withEmployer("Employer 3")
            .withPercent(60)
            .withType(EXP_TYPE_1)
            .withComment("Comment test 3")
            .withStartDate(LocalDate.of(2023, 7, 16))
            .withEndDate(LocalDate.of(2024, 7, 15))
            .build();

    public static final List<MemberOverview> MEMBER_1_OVERVIEWS = List
            .of(MemberOverview.Builder
                    .builder()
                    .withMemberId(MEMBER_1.getId())
                    .withFirstName(MEMBER_1.getFirstName())
                    .withLastName(MEMBER_1.getLastName())
                    .withAbbreviation(MEMBER_1.getAbbreviation())
                    .withEmploymentState(MEMBER_1.getEmploymentState())
                    .withDateOfHire(MEMBER_1.getDateOfHire())
                    .withBirthDate(MEMBER_1.getBirthDate())
                    .withOrganisationUnitName(MEMBER_1.getOrganisationUnit().getName())
                    .withCertificateId(CERTIFICATE_1.getId())
                    .withCertificateCompletedAt(CERTIFICATE_1.getCompletedAt())
                    .withCertificateComment(CERTIFICATE_1.getComment())
                    .withCertificateTypeName(CERTIFICATE_1.getCertificateType().getName())
                    .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                    .withDegreeId(DEGREE_1.getId())
                    .withDegreeName(DEGREE_1.getName())
                    .withDegreeStartDate(DEGREE_1.getStartDate())
                    .withDegreeEndDate(DEGREE_1.getEndDate())
                    .withDegreeTypeName(DEGREE_1.getDegreeType().getName())
                    .withExperienceId(EXPERIENCE_1.getId())
                    .withExperienceName(EXPERIENCE_1.getName())
                    .withExperienceEmployer(EXPERIENCE_1.getEmployer())
                    .withExperienceStartDate(EXPERIENCE_1.getStartDate())
                    .withExperienceEndDate(EXPERIENCE_1.getEndDate())
                    .withExperienceComment(EXPERIENCE_1.getComment())
                    .withExperienceTypeName(EXPERIENCE_1.getType().getName())
                    .build(),

                MemberOverview.Builder
                        .builder()
                        .withMemberId(MEMBER_1.getId())
                        .withFirstName(MEMBER_1.getFirstName())
                        .withLastName(MEMBER_1.getLastName())
                        .withAbbreviation(MEMBER_1.getAbbreviation())
                        .withEmploymentState(MEMBER_1.getEmploymentState())
                        .withDateOfHire(MEMBER_1.getDateOfHire())
                        .withBirthDate(MEMBER_1.getBirthDate())
                        .withOrganisationUnitName(MEMBER_1.getOrganisationUnit().getName())
                        .withCertificateId(CERTIFICATE_1.getId())
                        .withCertificateCompletedAt(CERTIFICATE_1.getCompletedAt())
                        .withCertificateComment(CERTIFICATE_1.getComment())
                        .withCertificateTypeName(CERTIFICATE_1.getCertificateType().getName())
                        .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                        .withDegreeId(DEGREE_1.getId())
                        .withDegreeName(DEGREE_1.getName())
                        .withDegreeStartDate(DEGREE_1.getStartDate())
                        .withDegreeEndDate(DEGREE_1.getEndDate())
                        .withDegreeTypeName(DEGREE_1.getDegreeType().getName())
                        .withExperienceId(EXPERIENCE_2.getId())
                        .withExperienceName(EXPERIENCE_2.getName())
                        .withExperienceEmployer(EXPERIENCE_2.getEmployer())
                        .withExperienceStartDate(EXPERIENCE_2.getStartDate())
                        .withExperienceEndDate(EXPERIENCE_2.getEndDate())
                        .withExperienceComment(EXPERIENCE_2.getComment())
                        .withExperienceTypeName(EXPERIENCE_2.getType().getName())
                        .build(),

                MemberOverview.Builder
                        .builder()
                        .withMemberId(MEMBER_1.getId())
                        .withFirstName(MEMBER_1.getFirstName())
                        .withLastName(MEMBER_1.getLastName())
                        .withAbbreviation(MEMBER_1.getAbbreviation())
                        .withEmploymentState(MEMBER_1.getEmploymentState())
                        .withDateOfHire(MEMBER_1.getDateOfHire())
                        .withBirthDate(MEMBER_1.getBirthDate())
                        .withOrganisationUnitName(MEMBER_1.getOrganisationUnit().getName())
                        .withCertificateId(CERTIFICATE_4.getId())
                        .withCertificateCompletedAt(CERTIFICATE_4.getCompletedAt())
                        .withCertificateComment(CERTIFICATE_4.getComment())
                        .withCertificateTypeName(CERTIFICATE_4.getCertificateType().getName())
                        .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                        .withDegreeId(DEGREE_1.getId())
                        .withDegreeName(DEGREE_1.getName())
                        .withDegreeStartDate(DEGREE_1.getStartDate())
                        .withDegreeEndDate(DEGREE_1.getEndDate())
                        .withDegreeTypeName(DEGREE_1.getDegreeType().getName())
                        .withExperienceId(EXPERIENCE_1.getId())
                        .withExperienceName(EXPERIENCE_1.getName())
                        .withExperienceEmployer(EXPERIENCE_1.getEmployer())
                        .withExperienceStartDate(EXPERIENCE_1.getStartDate())
                        .withExperienceEndDate(EXPERIENCE_1.getEndDate())
                        .withExperienceComment(EXPERIENCE_1.getComment())
                        .withExperienceTypeName(EXPERIENCE_1.getType().getName())
                        .build(),

                MemberOverview.Builder
                        .builder()
                        .withMemberId(MEMBER_1.getId())
                        .withFirstName(MEMBER_1.getFirstName())
                        .withLastName(MEMBER_1.getLastName())
                        .withAbbreviation(MEMBER_1.getAbbreviation())
                        .withEmploymentState(MEMBER_1.getEmploymentState())
                        .withDateOfHire(MEMBER_1.getDateOfHire())
                        .withBirthDate(MEMBER_1.getBirthDate())
                        .withOrganisationUnitName(MEMBER_1.getOrganisationUnit().getName())
                        .withCertificateId(CERTIFICATE_4.getId())
                        .withCertificateCompletedAt(CERTIFICATE_4.getCompletedAt())
                        .withCertificateComment(CERTIFICATE_4.getComment())
                        .withCertificateTypeName(CERTIFICATE_4.getCertificateType().getName())
                        .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                        .withDegreeId(DEGREE_1.getId())
                        .withDegreeName(DEGREE_1.getName())
                        .withDegreeStartDate(DEGREE_1.getStartDate())
                        .withDegreeEndDate(DEGREE_1.getEndDate())
                        .withDegreeTypeName(DEGREE_1.getDegreeType().getName())
                        .withExperienceId(EXPERIENCE_2.getId())
                        .withExperienceName(EXPERIENCE_2.getName())
                        .withExperienceEmployer(EXPERIENCE_2.getEmployer())
                        .withExperienceStartDate(EXPERIENCE_2.getStartDate())
                        .withExperienceEndDate(EXPERIENCE_2.getEndDate())
                        .withExperienceComment(EXPERIENCE_2.getComment())
                        .withExperienceTypeName(EXPERIENCE_2.getType().getName())
                        .build());

    public static final List<MemberOverview> MEMBER_2_OVERVIEWS = List
            .of(MemberOverview.Builder
                    .builder()
                    .withMemberId(MEMBER_2.getId())
                    .withFirstName(MEMBER_2.getFirstName())
                    .withLastName(MEMBER_2.getLastName())
                    .withAbbreviation(MEMBER_2.getAbbreviation())
                    .withEmploymentState(MEMBER_2.getEmploymentState())
                    .withDateOfHire(MEMBER_2.getDateOfHire())
                    .withBirthDate(MEMBER_2.getBirthDate())
                    .withOrganisationUnitName(MEMBER_2.getOrganisationUnit().getName())
                    .withCertificateId(CERTIFICATE_2.getId())
                    .withCertificateCompletedAt(CERTIFICATE_2.getCompletedAt())
                    .withCertificateComment(CERTIFICATE_2.getComment())
                    .withCertificateTypeName(CERTIFICATE_2.getCertificateType().getName())
                    .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                    .withDegreeId(DEGREE_2.getId())
                    .withDegreeName(DEGREE_2.getName())
                    .withDegreeStartDate(DEGREE_2.getStartDate())
                    .withDegreeEndDate(DEGREE_2.getEndDate())
                    .withDegreeTypeName(DEGREE_2.getDegreeType().getName())
                    .withExperienceId(EXPERIENCE_3.getId())
                    .withExperienceName(EXPERIENCE_3.getName())
                    .withExperienceEmployer(EXPERIENCE_3.getEmployer())
                    .withExperienceStartDate(EXPERIENCE_3.getStartDate())
                    .withExperienceEndDate(EXPERIENCE_3.getEndDate())
                    .withExperienceComment(EXPERIENCE_3.getComment())
                    .withExperienceTypeName(EXPERIENCE_3.getType().getName())
                    .build(),

                MemberOverview.Builder
                        .builder()
                        .withMemberId(MEMBER_2.getId())
                        .withFirstName(MEMBER_2.getFirstName())
                        .withLastName(MEMBER_2.getLastName())
                        .withAbbreviation(MEMBER_2.getAbbreviation())
                        .withEmploymentState(MEMBER_2.getEmploymentState())
                        .withDateOfHire(MEMBER_2.getDateOfHire())
                        .withBirthDate(MEMBER_2.getBirthDate())
                        .withOrganisationUnitName(MEMBER_2.getOrganisationUnit().getName())
                        .withCertificateId(CERTIFICATE_3.getId())
                        .withCertificateCompletedAt(CERTIFICATE_3.getCompletedAt())
                        .withCertificateComment(CERTIFICATE_3.getComment())
                        .withCertificateTypeName(CERTIFICATE_3.getCertificateType().getName())
                        .withleadershipTypeKind(CertificateKind.CERTIFICATE)
                        .withDegreeId(DEGREE_2.getId())
                        .withDegreeName(DEGREE_2.getName())
                        .withDegreeStartDate(DEGREE_2.getStartDate())
                        .withDegreeEndDate(DEGREE_2.getEndDate())
                        .withDegreeTypeName(DEGREE_2.getDegreeType().getName())
                        .withExperienceId(EXPERIENCE_3.getId())
                        .withExperienceName(EXPERIENCE_3.getName())
                        .withExperienceEmployer(EXPERIENCE_3.getEmployer())
                        .withExperienceStartDate(EXPERIENCE_3.getStartDate())
                        .withExperienceEndDate(EXPERIENCE_3.getEndDate())
                        .withExperienceComment(EXPERIENCE_3.getComment())
                        .withExperienceTypeName(EXPERIENCE_3.getType().getName())
                        .build());

    public static final List<MemberOverview> MEMBER_EMPTY_CV_OVERVIEWS = List
            .of(MemberOverview.Builder
                    .builder()
                    .withMemberId(MEMBER_1.getId())
                    .withFirstName(MEMBER_1.getFirstName())
                    .withLastName(MEMBER_1.getLastName())
                    .withEmploymentState(MEMBER_1.getEmploymentState())
                    .withAbbreviation(MEMBER_1.getAbbreviation())
                    .withDateOfHire(MEMBER_1.getDateOfHire())
                    .withBirthDate(MEMBER_1.getBirthDate())
                    .withOrganisationUnitName(MEMBER_1.getOrganisationUnit().getName())
                    // All CV IDs are NULL
                    .withCertificateId(null)
                    .withDegreeId(null)
                    .withExperienceId(null)
                    .build());

    public static final Calculation CALCULATION_1 = new Calculation(CALCULATION_1_ID,
                                                                    MEMBER_1,
                                                                    ROLE_2,
                                                                    CalculationState.DRAFT,
                                                                    LocalDate.of(2025, 1, 14),
                                                                    "Ldap User");

    public static final Calculation CALCULATION_2 = new Calculation(CALCULATION_2_ID,
                                                                    MEMBER_2,
                                                                    ROLE_2,
                                                                    CalculationState.ARCHIVED,
                                                                    LocalDate.of(2025, 1, 14),
                                                                    "Ldap User 2");

    public static final Calculation CALCULATION_3 = new Calculation(CALCULATION_3_ID,
                                                                    MEMBER_2,
                                                                    ROLE_2,
                                                                    CalculationState.ACTIVE,
                                                                    null,
                                                                    null);

    public static final RoleDto ROLE_2_DTO = new RoleDto(ROLE_2.getId(), ROLE_2.getName(), ROLE_2.getIsManagement());

    public static final RoleDto ROLE_2_INPUT = new RoleDto(null, ROLE_2.getName(), ROLE_2.getIsManagement());

    public static final OrganisationUnitDto ORG_UNIT_1_DTO = new OrganisationUnitDto(ORG_UNIT_1_ID,
                                                                                     ORG_UNIT_1.getName());

    public static final OrganisationUnitDto ORG_UNIT_2_DTO = new OrganisationUnitDto(ORG_UNIT_2_ID,
                                                                                     ORG_UNIT_2.getName());

    public static final OrganisationUnitDto ORG_UNIT_1_INPUT = new OrganisationUnitDto(null, ORG_UNIT_1.getName());

    public static final MemberDto MEMBER_1_DTO = new MemberDto(MEMBER_1_ID,
                                                               MEMBER_1.getFirstName(),
                                                               MEMBER_1.getLastName(),
                                                               MEMBER_1.getEmploymentState(),
                                                               MEMBER_1.getAbbreviation(),
                                                               MEMBER_1.getDateOfHire(),
                                                               MEMBER_1.getBirthDate(),
                                                               ORG_UNIT_1_DTO);

    public static final MemberDto MEMBER_2_DTO = new MemberDto(MEMBER_2_ID,
                                                               MEMBER_2.getFirstName(),
                                                               MEMBER_2.getLastName(),
                                                               MEMBER_2.getEmploymentState(),
                                                               MEMBER_2.getAbbreviation(),
                                                               MEMBER_2.getDateOfHire(),
                                                               MEMBER_2.getBirthDate(),
                                                               ORG_UNIT_2_DTO);

    public static final MemberInputDto MEMBER_1_INPUT = new MemberInputDto(MEMBER_1.getFirstName(),
                                                                           MEMBER_1.getLastName(),
                                                                           MEMBER_1.getEmploymentState(),
                                                                           MEMBER_1.getAbbreviation(),
                                                                           MEMBER_1.getDateOfHire(),
                                                                           MEMBER_1.getBirthDate(),
                                                                           MEMBER_1.getOrganisationUnit().getId());

    public static final MemberInputDto MEMBER_2_INPUT = new MemberInputDto(MEMBER_2.getFirstName(),
                                                                           MEMBER_2.getLastName(),
                                                                           MEMBER_2.getEmploymentState(),
                                                                           MEMBER_2.getAbbreviation(),
                                                                           MEMBER_2.getDateOfHire(),
                                                                           MEMBER_2.getBirthDate(),
                                                                           MEMBER_2.getOrganisationUnit().getId());

    public static final CertificateTypeDto CERT_TYPE_1_DTO = new CertificateTypeDto(CERT_TYPE_1_ID,
                                                                                    CERT_TYPE_1.getName(),
                                                                                    CERT_TYPE_1.getPoints(),
                                                                                    CERT_TYPE_1.getComment(),
                                                                                    CERT_TYPE_1
                                                                                            .getTags()
                                                                                            .stream()
                                                                                            .map(Tag::getName)
                                                                                            .toList());

    public static final CertificateTypeDto CERT_TYPE_5_DTO = new CertificateTypeDto(CERT_TYPE_5_ID,
                                                                                    CERT_TYPE_5.getName(),
                                                                                    CERT_TYPE_5.getPoints(),
                                                                                    CERT_TYPE_5.getComment(),
                                                                                    CERT_TYPE_5
                                                                                            .getTags()
                                                                                            .stream()
                                                                                            .map(Tag::getName)
                                                                                            .toList());

    public static final CertificateTypeDto CERT_TYPE_6_DTO = new CertificateTypeDto(CERT_TYPE_6_ID,
                                                                                    CERT_TYPE_6.getName(),
                                                                                    CERT_TYPE_6.getPoints(),
                                                                                    CERT_TYPE_6.getComment(),
                                                                                    CERT_TYPE_6
                                                                                            .getTags()
                                                                                            .stream()
                                                                                            .map(Tag::getName)
                                                                                            .toList());

    public static final CertificateTypeDto CERT_TYPE_5_Input = new CertificateTypeDto(null,
                                                                                      CERT_TYPE_5.getName(),
                                                                                      CERT_TYPE_5.getPoints(),
                                                                                      CERT_TYPE_5.getComment(),
                                                                                      CERT_TYPE_5
                                                                                              .getTags()
                                                                                              .stream()
                                                                                              .map(Tag::getName)
                                                                                              .toList());

    public final static LeadershipExperienceTypeDto LEADERSHIP_TYPE_1_DTO = new LeadershipExperienceTypeDto(LEADERSHIP_TYPE_1_ID,
                                                                                                            LEADERSHIP_TYPE_1
                                                                                                                    .getName(),
                                                                                                            LEADERSHIP_TYPE_1
                                                                                                                    .getPoints(),
                                                                                                            LEADERSHIP_TYPE_1
                                                                                                                    .getComment(),
                                                                                                            LEADERSHIP_TYPE_1
                                                                                                                    .getCertificateKind());

    public static final LeadershipExperienceTypeDto LEADERSHIP_TYPE_4_DTO = new LeadershipExperienceTypeDto(LEADERSHIP_TYPE_4_ID,
                                                                                                            LEADERSHIP_TYPE_4
                                                                                                                    .getName(),
                                                                                                            LEADERSHIP_TYPE_4
                                                                                                                    .getPoints(),
                                                                                                            LEADERSHIP_TYPE_4
                                                                                                                    .getComment(),
                                                                                                            LEADERSHIP_TYPE_4
                                                                                                                    .getCertificateKind());

    public static final LeadershipExperienceTypeDto LEADERSHIP_TYPE_5_DTO = new LeadershipExperienceTypeDto(LEADERSHIP_TYPE_5_ID,
                                                                                                            LEADERSHIP_TYPE_5
                                                                                                                    .getName(),
                                                                                                            LEADERSHIP_TYPE_5
                                                                                                                    .getPoints(),
                                                                                                            LEADERSHIP_TYPE_5
                                                                                                                    .getComment(),
                                                                                                            LEADERSHIP_TYPE_5
                                                                                                                    .getCertificateKind());

    public final static LeadershipExperienceTypeDto LEADERSHIP_TYPE_1_INPUT = new LeadershipExperienceTypeDto(null,
                                                                                                              LEADERSHIP_TYPE_1
                                                                                                                      .getName(),
                                                                                                              LEADERSHIP_TYPE_1
                                                                                                                      .getPoints(),
                                                                                                              LEADERSHIP_TYPE_1
                                                                                                                      .getComment(),
                                                                                                              LEADERSHIP_TYPE_1
                                                                                                                      .getCertificateKind());

    public static final CertificateDto CERTIFICATE_1_DTO = new CertificateDto(CERTIFICATE_1_ID,
                                                                              MEMBER_1_DTO,
                                                                              CERT_TYPE_1_DTO,
                                                                              CERTIFICATE_1.getCompletedAt(),
                                                                              CERTIFICATE_1.getValidUntil(),
                                                                              CERTIFICATE_1.getComment());

    public static final CertificateInputDto CERTIFICATE_1_INPUT = new CertificateInputDto(CERTIFICATE_1
            .getMember()
            .getId(),
                                                                                          CERTIFICATE_1
                                                                                                  .getCertificateType()
                                                                                                  .getId(),
                                                                                          CERTIFICATE_1
                                                                                                  .getCompletedAt(),
                                                                                          CERTIFICATE_1.getValidUntil(),
                                                                                          CERTIFICATE_1.getComment());

    public static final CertificateInputDto CERTIFICATE_2_INPUT = new CertificateInputDto(CERTIFICATE_2
            .getMember()
            .getId(),
                                                                                          CERTIFICATE_2
                                                                                                  .getCertificateType()
                                                                                                  .getId(),
                                                                                          CERTIFICATE_2
                                                                                                  .getCompletedAt(),
                                                                                          CERTIFICATE_2.getValidUntil(),
                                                                                          CERTIFICATE_2.getComment());

    public static final LeadershipExperienceDto LEADERSHIP_CERT_1_DTO = new LeadershipExperienceDto(LEADERSHIP_CERT_1_ID,
                                                                                                    MEMBER_1_DTO,
                                                                                                    LEADERSHIP_TYPE_1_DTO,
                                                                                                    LEADERSHIP_CERT_1
                                                                                                            .getComment());

    public final static LeadershipExperienceInputDto LEADERSHIP_CERT_1_INPUT = new LeadershipExperienceInputDto(LEADERSHIP_CERT_1
            .getMember()
            .getId(), LEADERSHIP_CERT_1.getCertificateType().getId(), LEADERSHIP_CERT_1.getComment());

    public final static LeadershipExperienceInputDto LEADERSHIP_CERT_2_INPUT = new LeadershipExperienceInputDto(LEADERSHIP_CERT_2
            .getMember()
            .getId(), LEADERSHIP_CERT_2.getCertificateType().getId(), LEADERSHIP_CERT_2.getComment());

    public static final DegreeTypeDto DEGREE_TYPE_1_DTO = new DegreeTypeDto(DEGREE_TYPE_1_ID,
                                                                            DEGREE_TYPE_1.getName(),
                                                                            DEGREE_TYPE_1.getHighlyRelevantPoints(),
                                                                            DEGREE_TYPE_1.getLimitedRelevantPoints(),
                                                                            DEGREE_TYPE_1.getLittleRelevantPoints());

    public static final DegreeTypeDto DEGREE_TYPE_1_INPUT = new DegreeTypeDto(null,
                                                                              DEGREE_TYPE_1.getName(),
                                                                              DEGREE_TYPE_1.getHighlyRelevantPoints(),
                                                                              DEGREE_TYPE_1.getLimitedRelevantPoints(),
                                                                              DEGREE_TYPE_1.getLittleRelevantPoints());

    public static final DegreeDto DEGREE_1_DTO = new DegreeDto(DEGREE_1.getId(),
                                                               MEMBER_1_DTO,
                                                               DEGREE_1.getName(),
                                                               DEGREE_1.getInstitution(),
                                                               DEGREE_1.getCompleted(),
                                                               DEGREE_TYPE_1_DTO,
                                                               DEGREE_1.getStartDate(),
                                                               DEGREE_1.getEndDate(),
                                                               DEGREE_1.getComment());

    public static final DegreeInputDto DEGREE_1_INPUT = new DegreeInputDto(DEGREE_1.getMember().getId(),
                                                                           DEGREE_1.getName(),
                                                                           DEGREE_1.getInstitution(),
                                                                           DEGREE_1.getCompleted(),
                                                                           DEGREE_1.getDegreeType().getId(),
                                                                           DEGREE_1.getStartDate(),
                                                                           DEGREE_1.getEndDate(),
                                                                           DEGREE_1.getComment());

    public static final DegreeInputDto DEGREE_2_INPUT = new DegreeInputDto(DEGREE_2.getMember().getId(),
                                                                           DEGREE_2.getName(),
                                                                           DEGREE_2.getInstitution(),
                                                                           DEGREE_2.getCompleted(),
                                                                           DEGREE_2.getDegreeType().getId(),
                                                                           DEGREE_2.getStartDate(),
                                                                           DEGREE_2.getEndDate(),
                                                                           DEGREE_2.getComment());

    public static final ExperienceTypeDto EXP_TYPE_1_DTO = new ExperienceTypeDto(EXP_TYPE_1_ID,
                                                                                 EXP_TYPE_1.getName(),
                                                                                 EXP_TYPE_1.getHighlyRelevantPoints(),
                                                                                 EXP_TYPE_1.getLimitedRelevantPoints(),
                                                                                 EXP_TYPE_1.getLittleRelevantPoints());

    public static final ExperienceTypeDto EXP_TYPE_1_INPUT = new ExperienceTypeDto(null,
                                                                                   EXP_TYPE_1.getName(),
                                                                                   EXP_TYPE_1.getHighlyRelevantPoints(),
                                                                                   EXP_TYPE_1
                                                                                           .getLimitedRelevantPoints(),
                                                                                   EXP_TYPE_1
                                                                                           .getLittleRelevantPoints());

    public static final ExperienceDto EXPERIENCE_1_DTO = new ExperienceDto(EXPERIENCE_1_ID,
                                                                           MEMBER_1_DTO,
                                                                           EXPERIENCE_1.getName(),
                                                                           EXPERIENCE_1.getEmployer(),
                                                                           EXPERIENCE_1.getPercent(),
                                                                           EXP_TYPE_1_DTO,
                                                                           EXPERIENCE_1.getComment(),
                                                                           EXPERIENCE_1.getStartDate(),
                                                                           EXPERIENCE_1.getEndDate());

    public static final ExperienceInputDto EXPERIENCE_1_INPUT = new ExperienceInputDto(EXPERIENCE_1.getMember().getId(),
                                                                                       EXPERIENCE_1.getName(),
                                                                                       EXPERIENCE_1.getEmployer(),
                                                                                       EXPERIENCE_1.getPercent(),
                                                                                       EXPERIENCE_1.getType().getId(),
                                                                                       EXPERIENCE_1.getComment(),
                                                                                       EXPERIENCE_1.getStartDate(),
                                                                                       EXPERIENCE_1.getEndDate());

    public static final ExperienceInputDto EXPERIENCE_2_INPUT = new ExperienceInputDto(EXPERIENCE_2.getMember().getId(),
                                                                                       EXPERIENCE_2.getName(),
                                                                                       EXPERIENCE_2.getEmployer(),
                                                                                       EXPERIENCE_2.getPercent(),
                                                                                       EXPERIENCE_2.getType().getId(),
                                                                                       EXPERIENCE_2.getComment(),
                                                                                       EXPERIENCE_2.getStartDate(),
                                                                                       EXPERIENCE_2.getEndDate());

    public static final MemberOverviewDto MEMBER_1_OVERVIEW_DTO = new MemberOverviewDto(new MemberOverviewMemberDto(MEMBER_1
            .getId(),
                                                                                                                    MEMBER_1
                                                                                                                            .getFirstName(),
                                                                                                                    MEMBER_1
                                                                                                                            .getLastName(),
                                                                                                                    MEMBER_1
                                                                                                                            .getEmploymentState(),
                                                                                                                    MEMBER_1
                                                                                                                            .getAbbreviation(),
                                                                                                                    MEMBER_1
                                                                                                                            .getDateOfHire(),
                                                                                                                    MEMBER_1
                                                                                                                            .getBirthDate(),
                                                                                                                    MEMBER_1
                                                                                                                            .getOrganisationUnit()
                                                                                                                            .getName()),
                                                                                        new MemberOverviewCvDto(List
                                                                                                .of(new MemberOverviewDegreeDto(DEGREE_1
                                                                                                        .getId(),
                                                                                                                                DEGREE_1
                                                                                                                                        .getName(),
                                                                                                                                DEGREE_1
                                                                                                                                        .getDegreeType()
                                                                                                                                        .getName(),
                                                                                                                                DEGREE_1
                                                                                                                                        .getStartDate(),
                                                                                                                                DEGREE_1
                                                                                                                                        .getEndDate())),
                                                                                                                List
                                                                                                                        .of(new MemberOverviewExperienceDto(EXPERIENCE_1
                                                                                                                                .getId(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getEmployer(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getType()
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getComment(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getStartDate(),
                                                                                                                                                            EXPERIENCE_1
                                                                                                                                                                    .getEndDate()),
                                                                                                                            new MemberOverviewExperienceDto(EXPERIENCE_2
                                                                                                                                    .getId(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getEmployer(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getType()
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getComment(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getStartDate(),
                                                                                                                                                            EXPERIENCE_2
                                                                                                                                                                    .getEndDate())),
                                                                                                                List
                                                                                                                        .of(new MemberOverviewCertificateDto(CERTIFICATE_1
                                                                                                                                .getId(),
                                                                                                                                                             CERTIFICATE_1
                                                                                                                                                                     .getCertificateType()
                                                                                                                                                                     .getName(),
                                                                                                                                                             CERTIFICATE_1
                                                                                                                                                                     .getCompletedAt(),
                                                                                                                                                             CERTIFICATE_1
                                                                                                                                                                     .getComment()),
                                                                                                                            new MemberOverviewCertificateDto(CERTIFICATE_4
                                                                                                                                    .getId(),
                                                                                                                                                             CERTIFICATE_4
                                                                                                                                                                     .getCertificateType()
                                                                                                                                                                     .getName(),
                                                                                                                                                             CERTIFICATE_4
                                                                                                                                                                     .getCompletedAt(),
                                                                                                                                                             CERTIFICATE_4
                                                                                                                                                                     .getComment())),
                                                                                                                List
                                                                                                                        .of()));

    public static final MemberOverviewDto MEMBER_2_OVERVIEW_DTO = new MemberOverviewDto(new MemberOverviewMemberDto(MEMBER_2
            .getId(),
                                                                                                                    MEMBER_2
                                                                                                                            .getFirstName(),
                                                                                                                    MEMBER_2
                                                                                                                            .getLastName(),
                                                                                                                    MEMBER_2
                                                                                                                            .getEmploymentState(),
                                                                                                                    MEMBER_2
                                                                                                                            .getAbbreviation(),
                                                                                                                    MEMBER_2
                                                                                                                            .getDateOfHire(),
                                                                                                                    MEMBER_2
                                                                                                                            .getBirthDate(),
                                                                                                                    MEMBER_2
                                                                                                                            .getOrganisationUnit()
                                                                                                                            .getName()),
                                                                                        new MemberOverviewCvDto(List
                                                                                                .of(new MemberOverviewDegreeDto(DEGREE_2
                                                                                                        .getId(),
                                                                                                                                DEGREE_2
                                                                                                                                        .getName(),
                                                                                                                                DEGREE_2
                                                                                                                                        .getDegreeType()
                                                                                                                                        .getName(),
                                                                                                                                DEGREE_2
                                                                                                                                        .getStartDate(),
                                                                                                                                DEGREE_2
                                                                                                                                        .getEndDate())),
                                                                                                                List
                                                                                                                        .of(new MemberOverviewExperienceDto(EXPERIENCE_3
                                                                                                                                .getId(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getEmployer(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getType()
                                                                                                                                                                    .getName(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getComment(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getStartDate(),
                                                                                                                                                            EXPERIENCE_3
                                                                                                                                                                    .getEndDate())),
                                                                                                                List
                                                                                                                        .of(new MemberOverviewCertificateDto(CERTIFICATE_2
                                                                                                                                .getId(),
                                                                                                                                                             CERTIFICATE_2
                                                                                                                                                                     .getCertificateType()
                                                                                                                                                                     .getName(),
                                                                                                                                                             CERTIFICATE_2
                                                                                                                                                                     .getCompletedAt(),
                                                                                                                                                             CERTIFICATE_2
                                                                                                                                                                     .getComment()),
                                                                                                                            new MemberOverviewCertificateDto(CERTIFICATE_3
                                                                                                                                    .getId(),
                                                                                                                                                             CERTIFICATE_3
                                                                                                                                                                     .getCertificateType()
                                                                                                                                                                     .getName(),
                                                                                                                                                             CERTIFICATE_3
                                                                                                                                                                     .getCompletedAt(),
                                                                                                                                                             CERTIFICATE_3
                                                                                                                                                                     .getComment())),
                                                                                                                List
                                                                                                                        .of()));

    public static final MemberOverviewDto MEMBER_EMPTY_CV_DTO = new MemberOverviewDto(new MemberOverviewMemberDto(MEMBER_1
            .getId(),
                                                                                                                  MEMBER_1
                                                                                                                          .getFirstName(),
                                                                                                                  MEMBER_1
                                                                                                                          .getLastName(),
                                                                                                                  MEMBER_1
                                                                                                                          .getEmploymentState(),
                                                                                                                  MEMBER_1
                                                                                                                          .getAbbreviation(),
                                                                                                                  MEMBER_1
                                                                                                                          .getDateOfHire(),
                                                                                                                  MEMBER_1
                                                                                                                          .getBirthDate(),
                                                                                                                  MEMBER_1
                                                                                                                          .getOrganisationUnit()
                                                                                                                          .getName()),
                                                                                      new MemberOverviewCvDto(List.of(),
                                                                                                              List.of(),
                                                                                                              List.of(),
                                                                                                              List
                                                                                                                      .of()));

    public static final CalculationDto CALCULATION_DTO_1 = new CalculationDto(CALCULATION_1_ID,
                                                                              MEMBER_1_DTO,
                                                                              ROLE_2_DTO,
                                                                              CALCULATION_1.getState(),
                                                                              CALCULATION_1.getPublicationDate(),
                                                                              CALCULATION_1.getPublicizedBy());

    public static final CalculationInputDto CALCULATION_INPUT_DTO_1 = new CalculationInputDto(CALCULATION_1
            .getMember()
            .getId(), CALCULATION_1.getRole().getId(), CALCULATION_1.getState());

    public static final CalculationInputDto CALCULATION_INPUT_DTO_2 = new CalculationInputDto(CALCULATION_2
            .getMember()
            .getId(), CALCULATION_2.getRole().getId(), CALCULATION_2.getState());

    public static final List<OrganisationUnit> ORGANISATION_UNITS = List.of(ORG_UNIT_2);

    public static final List<Role> ROLES = List.of(ROLE_2);

    public static final List<Tag> TAGS_1 = List.of(TAG_1, TAG_2);

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
