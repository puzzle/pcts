package ch.puzzle.pcts.util;

import static ch.puzzle.pcts.util.TestData.*;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
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

public class TestDataModels {

    private static final LocalDateTime UNIX_EPOCH = LocalDateTime.of(1970, 1, 1, 0, 0);

    private TestDataModels() {
    }

    public static final OrganisationUnit ORG_UNIT_1;
    static {
        ORG_UNIT_1 = OrganisationUnit.Builder.builder().withId(ORG_UNIT_1_ID).withName("OrganisationUnit 1").build();
        ORG_UNIT_1.setDeletedAt(UNIX_EPOCH);
    }

    public static final OrganisationUnit ORG_UNIT_2 = OrganisationUnit.Builder
            .builder()
            .withId(ORG_UNIT_2_ID)
            .withName("OrganisationUnit 2")
            .build();

    public static final Role ROLE_2 = Role.Builder
            .builder()
            .withId(ROLE_2_ID)
            .withName("Role 2")
            .withIsManagement(false)
            .build();

    public static final Role ROLE_1;
    static {
        ROLE_1 = new Role(ROLE_1_ID, "Role 1", true);
        ROLE_1.setDeletedAt(UNIX_EPOCH);
    }

    public static final Tag TAG_1 = Tag.Builder.builder().withId(TAG_1_ID).withName("Tag 1").build();

    public static final Tag TAG_2 = Tag.Builder.builder().withId(TAG_2_ID).withName("Longer tag name").build();

    public static final Tag TAG_3 = Tag.Builder.builder().withName("First tag without id").build();

    public static final Tag TAG_4 = Tag.Builder.builder().withName("Second tag without id").build();

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

    public static final CertificateType CERT_TYPE_1 = CertificateType.Builder
            .builder()
            .withId(CERT_TYPE_1_ID)
            .withName("Certificate Type 1")
            .withPoints(BigDecimal.valueOf(5.5))
            .withComment("This is Certificate 1")
            .withTags(Set.of(TAG_1))
            .withCertificateKind(CertificateKind.CERTIFICATE)
            .build();

    public static final CertificateType CERT_TYPE_2 = CertificateType.Builder
            .builder()
            .withId(CERT_TYPE_2_ID)
            .withName("Certificate Type 2")
            .withPoints(BigDecimal.valueOf(1))
            .withComment("This is Certificate 2")
            .withTags(Set.of(TAG_2))
            .withCertificateKind(CertificateKind.CERTIFICATE)
            .build();

    public static final CertificateType CERT_TYPE_3 = CertificateType.Builder
            .builder()
            .withId(CERT_TYPE_3_ID)
            .withName("Certificate Type 3")
            .withPoints(BigDecimal.valueOf(3))
            .withComment("This is Certificate 3")
            .withTags(Set.of())
            .withCertificateKind(CertificateKind.CERTIFICATE)
            .build();

    public static final CertificateType CERT_TYPE_4 = CertificateType.Builder
            .builder()
            .withId(CERT_TYPE_4_ID)
            .withName("Certificate Type 4")
            .withPoints(BigDecimal.valueOf(0.5))
            .withComment("This is Certificate 4")
            .withTags(Set.of())
            .withCertificateKind(CertificateKind.CERTIFICATE)
            .build();

    public static final CertificateType CERT_TYPE_5 = CertificateType.Builder
            .builder()
            .withId(CERT_TYPE_5_ID)
            .withName("Certificate Type 5")
            .withPoints(BigDecimal.valueOf(0.5))
            .withComment("This is Certificate 5")
            .withTags(Set.of(TAG_3, TAG_4))
            .withCertificateKind(CertificateKind.CERTIFICATE)
            .build();

    public static final CertificateType CERT_TYPE_6 = CertificateType.Builder
            .builder()
            .withId(CERT_TYPE_6_ID)
            .withName("Certificate Type 6")
            .withPoints(BigDecimal.valueOf(0.5))
            .withComment("This is Certificate 6")
            .withTags(Set.of(TAG_3, TAG_4))
            .withCertificateKind(CertificateKind.CERTIFICATE)
            .build();

    public static final CertificateType LEADERSHIP_TYPE_1 = CertificateType.Builder
            .builder()
            .withId(LEADERSHIP_TYPE_1_ID)
            .withName("LeadershipExperience Type 1")
            .withPoints(BigDecimal.valueOf(5.5))
            .withComment("This is LeadershipExperience 1")
            .withTags(Set.of())
            .withCertificateKind(CertificateKind.MILITARY_FUNCTION)
            .build();

    public static final CertificateType LEADERSHIP_TYPE_2 = CertificateType.Builder
            .builder()
            .withId(LEADERSHIP_TYPE_2_ID)
            .withName("LeadershipExperience Type 2")
            .withPoints(BigDecimal.valueOf(1))
            .withComment("This is LeadershipExperience 2")
            .withTags(Set.of())
            .withCertificateKind(CertificateKind.YOUTH_AND_SPORT)
            .build();

    public static final CertificateType LEADERSHIP_TYPE_3 = CertificateType.Builder
            .builder()
            .withId(LEADERSHIP_TYPE_3_ID)
            .withName("LeadershipExperience Type 3")
            .withPoints(BigDecimal.valueOf(3))
            .withComment("This is LeadershipExperience 3")
            .withTags(Set.of())
            .withCertificateKind(CertificateKind.LEADERSHIP_TRAINING)
            .build();

    public static final CertificateType LEADERSHIP_TYPE_4 = CertificateType.Builder
            .builder()
            .withId(LEADERSHIP_TYPE_4_ID)
            .withName("LeadershipExperience Type 4")
            .withPoints(BigDecimal.valueOf(4))
            .withComment("This is a comment.")
            .withTags(null)
            .withCertificateKind(CertificateKind.YOUTH_AND_SPORT)
            .build();

    public static final CertificateType LEADERSHIP_TYPE_5 = CertificateType.Builder
            .builder()
            .withId(LEADERSHIP_TYPE_5_ID)
            .withName("LeadershipExperience Type 5")
            .withPoints(BigDecimal.valueOf(2))
            .withComment("This is a comment.")
            .withTags(null)
            .withCertificateKind(CertificateKind.MILITARY_FUNCTION)
            .build();

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
            .withCompletedAt(LocalDate.of(2010, 8, 12))
            .withValidUntil(LocalDate.of(2023, 3, 25))
            .withComment("Left organization.")
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

    public static final DegreeType DEGREE_TYPE_1 = DegreeType.Builder
            .builder()
            .withId(DEGREE_1_ID)
            .withName("Degree type 1")
            .withHighlyRelevantPoints(BigDecimal.valueOf(120.55))
            .withLimitedRelevantPoints(BigDecimal.valueOf(60))
            .withLittleRelevantPoints(BigDecimal.valueOf(15.45))
            .build();

    public static final DegreeType DEGREE_TYPE_2 = DegreeType.Builder
            .builder()
            .withId(DEGREE_2_ID)
            .withName("Degree type 2")
            .withHighlyRelevantPoints(BigDecimal.valueOf(12))
            .withLimitedRelevantPoints(BigDecimal.valueOf(3.961))
            .withLittleRelevantPoints(BigDecimal.valueOf(3))
            .build();

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

    public static final ExperienceType EXP_TYPE_1 = ExperienceType.Builder
            .builder()
            .withId(EXP_TYPE_1_ID)
            .withName("ExperienceType 1")
            .withHighlyRelevantPoints(BigDecimal.ZERO)
            .withLimitedRelevantPoints(BigDecimal.valueOf(12))
            .withLittleRelevantPoints(BigDecimal.valueOf(4.005))
            .build();

    public static final ExperienceType EXP_TYPE_2 = ExperienceType.Builder
            .builder()
            .withId(EXP_TYPE_2_ID)
            .withName("ExperienceType 2")
            .withHighlyRelevantPoints(BigDecimal.valueOf(12))
            .withLimitedRelevantPoints(BigDecimal.valueOf(10.7989))
            .withLittleRelevantPoints(BigDecimal.valueOf(6))
            .build();

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
                        .withCertificateId(LEADERSHIP_CERT_1.getId())
                        .withCertificateCompletedAt(LEADERSHIP_CERT_1.getCompletedAt())
                        .withCertificateComment(LEADERSHIP_CERT_1.getComment())
                        .withCertificateTypeName(LEADERSHIP_CERT_1.getCertificateType().getName())
                        .withleadershipTypeKind(LEADERSHIP_CERT_1.getCertificateType().getCertificateKind())
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
                        .withCertificateId(LEADERSHIP_CERT_1.getId())
                        .withCertificateCompletedAt(LEADERSHIP_CERT_1.getCompletedAt())
                        .withCertificateComment(LEADERSHIP_CERT_1.getComment())
                        .withCertificateTypeName(LEADERSHIP_CERT_1.getCertificateType().getName())
                        .withleadershipTypeKind(LEADERSHIP_CERT_1.getCertificateType().getCertificateKind())
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

    public static final DegreeCalculation DEGREE_CALC_1 = new DegreeCalculation(DEGREE_CALC_1_ID,
                                                                                null,
                                                                                DEGREE_2,
                                                                                Relevancy.STRONGLY,
                                                                                BigDecimal.valueOf(80),
                                                                                "Comment");

    public static final DegreeCalculation DEGREE_CALC_2 = new DegreeCalculation(DEGREE_CALC_2_ID,
                                                                                null,
                                                                                DEGREE_2,
                                                                                Relevancy.POORLY,
                                                                                BigDecimal.valueOf(10),
                                                                                "Comment");

    public static final DegreeCalculation DEGREE_CALC_3 = new DegreeCalculation(DEGREE_CALC_3_ID,
                                                                                null,
                                                                                DEGREE_2,
                                                                                Relevancy.NORMAL,
                                                                                BigDecimal.valueOf(100),
                                                                                "Comment");

    public static final ExperienceCalculation EXP_CALC_1 = new ExperienceCalculation(EXPERIENCE_CALC_1_ID,
                                                                                     null,
                                                                                     EXPERIENCE_2,
                                                                                     Relevancy.STRONGLY,
                                                                                     "Comment");

    public static final ExperienceCalculation EXP_CALC_2 = new ExperienceCalculation(EXPERIENCE_CALC_2_ID,
                                                                                     null,
                                                                                     EXPERIENCE_2,
                                                                                     Relevancy.POORLY,
                                                                                     "Comment");

    public static final ExperienceCalculation EXP_CALC_3 = new ExperienceCalculation(EXPERIENCE_CALC_3_ID,
                                                                                     null,
                                                                                     EXPERIENCE_3,
                                                                                     Relevancy.NORMAL,
                                                                                     "Comment");

    public static final CertificateCalculation CERT_CALC_1 = new CertificateCalculation(CERTIFICATE_CALC_1_ID,
                                                                                        null,
                                                                                        CERTIFICATE_2);

    public static final CertificateCalculation CERT_CALC_2 = new CertificateCalculation(CERTIFICATE_CALC_2_ID,
                                                                                        null,
                                                                                        CERTIFICATE_2);

    public static final CertificateCalculation CERT_CALC_3 = new CertificateCalculation(CERTIFICATE_CALC_3_ID,
                                                                                        null,
                                                                                        LEADERSHIP_CERT_1);

    public static final Calculation CALCULATION_1;
    public static final Calculation CALCULATION_2;
    public static final Calculation CALCULATION_3;

    static {

        Calculation calc1 = Calculation.Builder
                .builder()
                .withId(CALCULATION_1_ID)
                .withMember(MEMBER_1)
                .withRole(ROLE_2)
                .withState(CalculationState.DRAFT)
                .withPublicationDate(LocalDate.of(2025, 1, 14))
                .withPublicizedBy("Ldap User")
                .withDegreeCalculations(List.of(DEGREE_CALC_1, DEGREE_CALC_3))
                .withExperienceCalculations(List.of(EXP_CALC_1, EXP_CALC_3))
                .withCertificateCalculations(List.of(CERT_CALC_1, CERT_CALC_3))
                .build();

        calc1.getDegreeCalculations().forEach(d -> d.setCalculation(calc1));
        calc1.getExperienceCalculations().forEach(e -> e.setCalculation(calc1));
        calc1.getCertificateCalculations().forEach(c -> c.setCalculation(calc1));
        CALCULATION_1 = calc1;

        Calculation calc2 = Calculation.Builder
                .builder()
                .withId(CALCULATION_2_ID)
                .withMember(MEMBER_2)
                .withRole(ROLE_2)
                .withState(CalculationState.ARCHIVED)
                .withPublicationDate(LocalDate.of(2025, 1, 14))
                .withPublicizedBy("Ldap User 2")
                .withDegreeCalculations(List.of(DEGREE_CALC_2))
                .withExperienceCalculations(List.of(EXP_CALC_2))
                .withCertificateCalculations(List.of(CERT_CALC_2))
                .build();

        calc2.getDegreeCalculations().forEach(d -> d.setCalculation(calc2));
        calc2.getExperienceCalculations().forEach(e -> e.setCalculation(calc2));
        calc2.getCertificateCalculations().forEach(c -> c.setCalculation(calc2));
        CALCULATION_2 = calc2;

        CALCULATION_3 = Calculation.Builder
                .builder()
                .withId(CALCULATION_3_ID)
                .withMember(MEMBER_2)
                .withRole(ROLE_2)
                .withState(CalculationState.ACTIVE)
                .withDegreeCalculations(List.of())
                .withExperienceCalculations(List.of())
                .withCertificateCalculations(List.of())
                .build();
    }

    public static final List<OrganisationUnit> ORGANISATION_UNITS = List.of(ORG_UNIT_2);

    public static final List<Role> ROLES = List.of(ROLE_2);

    public static final List<Tag> TAGS_1 = List.of(TAG_1, TAG_2);

    public static final List<Member> MEMBERS = List.of(MEMBER_1, MEMBER_2);

    public static final List<CertificateType> CERTIFICATE_TYPES = List
            .of(CERT_TYPE_1, CERT_TYPE_2, CERT_TYPE_3, CERT_TYPE_4);

    public static final List<CertificateType> LEADERSHIP_EXPERIENCE_TYPES = List
            .of(LEADERSHIP_TYPE_1, LEADERSHIP_TYPE_2, LEADERSHIP_TYPE_3);

    public static final List<Certificate> CERTIFICATES = List
            .of(CERTIFICATE_1, CERTIFICATE_2, CERTIFICATE_3, CERTIFICATE_4, LEADERSHIP_CERT_1);

    public static final List<DegreeType> DEGREE_TYPES = List.of(DEGREE_TYPE_1, DEGREE_TYPE_2);

    public static final List<Degree> DEGREES = List.of(DEGREE_1, DEGREE_2);

    public static final List<ExperienceType> EXPERIENCE_TYPES = List.of(EXP_TYPE_1, EXP_TYPE_2);

    public static final List<Experience> EXPERIENCES = List.of(EXPERIENCE_2, EXPERIENCE_3);

    public static final List<DegreeCalculation> DEGREE_CALCULATIONS = List
            .of(DEGREE_CALC_1, DEGREE_CALC_2, DEGREE_CALC_3);

    public static final List<ExperienceCalculation> EXPERIENCE_CALCULATIONS = List
            .of(EXP_CALC_1, EXP_CALC_2, EXP_CALC_3);

    public static final List<CertificateCalculation> CERTIFICATE_CALCULATIONS = List
            .of(CERT_CALC_1, CERT_CALC_2, CERT_CALC_3);

    public static final List<Calculation> CALCULATIONS = List.of(CALCULATION_1, CALCULATION_2, CALCULATION_3);
}
