package ch.puzzle.pcts.model.memberoverview;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.member.EmploymentState;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "member_overview_view")
public class MemberOverview implements Model {

    @Id
    @Column(name = "unique_row_id")
    private Long uniqueRowId;

    private Long memberId;
    private String firstName;
    private String lastName;
    private String abbreviation;

    private LocalDate birthDate;
    private LocalDate dateOfHire;
    @Enumerated(EnumType.STRING)
    private EmploymentState employmentState;

    private Long organisationUnitId;
    private String organisationUnitName;

    private Long certificateId;
    private LocalDate certificateCompletedAt;
    private LocalDate certificateValidUntil;
    private String certificateComment;

    private Long certificateTypeId;
    private String certificateTypeName;
    private BigDecimal certificateTypePoints;
    private String certificateTypeComment;

    private Long tagId;
    private String tagName;

    private Long leadershipId;
    private String leadershipComment;

    private Long degreeId;
    private String degreeName;
    private LocalDate degreeStartDate;
    private LocalDate degreeEndDate;
    private String degreeInstitution;
    private Boolean degreeCompleted;
    private String degreeComment;
    private Long leadershipTypeId;
    private String leadershipTypeName;

    @Enumerated(EnumType.STRING)
    private CertificateKind leadershipKind;
    private BigDecimal leadershipTypePoints;
    private String leadershipTypeComment;

    private Long degreeTypeId;
    private String degreeTypeName;
    private BigDecimal degreeHighlyRelevantPoints;
    private BigDecimal degreeLimitedRelevantPoints;
    private BigDecimal degreeLittleRelevantPoints;

    private Long experienceId;
    private String experienceName;
    private String experienceEmployer;
    private LocalDate experienceStartDate;
    private LocalDate experienceEndDate;
    private Integer experiencePercent;
    private String experienceComment;

    private Long experienceTypeId;
    private String experienceTypeName;
    private BigDecimal experienceHighlyRelevantPoints;
    private BigDecimal experienceLimitedRelevantPoints;
    private BigDecimal experienceLittleRelevantPoints;

    private Long calculationId;
    @Enumerated(EnumType.STRING)
    private CalculationState calculationState;
    private LocalDate calculationPublicationDate;
    private String calculationPublicizedBy;

    private Long roleId;
    private String roleName;
    private Boolean roleIsManagement;

    private MemberOverview(Builder builder) {
        uniqueRowId = builder.uniqueRowId;
        memberId = builder.memberId;
        firstName = trim(builder.firstName);
        lastName = trim(builder.lastName);
        abbreviation = trim(builder.abbreviation);
        birthDate = builder.birthDate;
        dateOfHire = builder.dateOfHire;
        employmentState = builder.employmentState;
        organisationUnitId = builder.organisationUnitId;
        organisationUnitName = trim(builder.organisationUnitName);
        certificateId = builder.certificateId;
        certificateCompletedAt = builder.certificateCompletedAt;
        certificateValidUntil = builder.certificateValidUntil;
        certificateComment = trim(builder.certificateComment);
        certificateTypeId = builder.certificateTypeId;
        certificateTypeName = trim(builder.certificateTypeName);
        certificateTypePoints = builder.certificateTypePoints;
        certificateTypeComment = trim(builder.certificateTypeComment);
        tagId = builder.tagId;
        tagName = trim(builder.tagName);
        leadershipId = builder.leadershipId;
        leadershipComment = trim(builder.leadershipComment);
        degreeId = builder.degreeId;
        degreeName = trim(builder.degreeName);
        degreeStartDate = builder.degreeStartDate;
        degreeEndDate = builder.degreeEndDate;
        degreeInstitution = trim(builder.degreeInstitution);
        degreeCompleted = builder.degreeCompleted;
        degreeComment = trim(builder.degreeComment);
        leadershipTypeId = builder.leadershipTypeId;
        leadershipTypeName = trim(builder.leadershipTypeName);
        leadershipKind = builder.leadershipKind;
        leadershipTypePoints = builder.leadershipTypePoints;
        leadershipTypeComment = trim(builder.leadershipTypeComment);
        degreeTypeId = builder.degreeTypeId;
        degreeTypeName = trim(builder.degreeTypeName);
        degreeHighlyRelevantPoints = builder.degreeHighlyRelevantPoints;
        degreeLimitedRelevantPoints = builder.degreeLimitedRelevantPoints;
        degreeLittleRelevantPoints = builder.degreeLittleRelevantPoints;
        experienceId = builder.experienceId;
        experienceName = trim(builder.experienceName);
        experienceEmployer = trim(builder.experienceEmployer);
        experienceStartDate = builder.experienceStartDate;
        experienceEndDate = builder.experienceEndDate;
        experiencePercent = builder.experiencePercent;
        experienceComment = trim(builder.experienceComment);
        experienceTypeId = builder.experienceTypeId;
        experienceTypeName = trim(builder.experienceTypeName);
        experienceHighlyRelevantPoints = builder.experienceHighlyRelevantPoints;
        experienceLimitedRelevantPoints = builder.experienceLimitedRelevantPoints;
        experienceLittleRelevantPoints = builder.experienceLittleRelevantPoints;
        calculationId = builder.calculationId;
        calculationState = builder.calculationState;
        calculationPublicationDate = builder.calculationPublicationDate;
        calculationPublicizedBy = trim(builder.calculationPublicizedBy);
        roleId = builder.roleId;
        roleName = trim(builder.roleName);
        roleIsManagement = builder.roleIsManagement;
    }

    public MemberOverview() {

    }

    @Override
    public Long getId() {
        return uniqueRowId;
    }

    @Override
    public void setId(Long uniqueRowId) {
        this.uniqueRowId = uniqueRowId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = trim(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = trim(lastName);
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = trim(abbreviation);
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(LocalDate dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public EmploymentState getEmploymentState() {
        return employmentState;
    }

    public void setEmploymentState(EmploymentState employmentState) {
        this.employmentState = employmentState;
    }

    public Long getOrganisationUnitId() {
        return organisationUnitId;
    }

    public void setOrganisationUnitId(Long organisationUnitId) {
        this.organisationUnitId = organisationUnitId;
    }

    public String getOrganisationUnitName() {
        return organisationUnitName;
    }

    public void setOrganisationUnitName(String organisationUnitName) {
        this.organisationUnitName = trim(organisationUnitName);
    }

    public Long getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Long certificateId) {
        this.certificateId = certificateId;
    }

    public LocalDate getCertificateCompletedAt() {
        return certificateCompletedAt;
    }

    public void setCertificateCompletedAt(LocalDate certificateCompletedAt) {
        this.certificateCompletedAt = certificateCompletedAt;
    }

    public LocalDate getCertificateValidUntil() {
        return certificateValidUntil;
    }

    public void setCertificateValidUntil(LocalDate certificateValidUntil) {
        this.certificateValidUntil = certificateValidUntil;
    }

    public String getCertificateComment() {
        return certificateComment;
    }

    public void setCertificateComment(String certificateComment) {
        this.certificateComment = trim(certificateComment);
    }

    public Long getCertificateTypeId() {
        return certificateTypeId;
    }

    public void setCertificateTypeId(Long certificateTypeId) {
        this.certificateTypeId = certificateTypeId;
    }

    public String getCertificateTypeName() {
        return certificateTypeName;
    }

    public void setCertificateTypeName(String certificateTypeName) {
        this.certificateTypeName = trim(certificateTypeName);
    }

    public BigDecimal getCertificateTypePoints() {
        return certificateTypePoints;
    }

    public void setCertificateTypePoints(BigDecimal certificateTypePoints) {
        this.certificateTypePoints = certificateTypePoints;
    }

    public String getCertificateTypeComment() {
        return certificateTypeComment;
    }

    public void setCertificateTypeComment(String certificateTypeComment) {
        this.certificateTypeComment = trim(certificateTypeComment);
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = trim(tagName);
    }

    public Long getLeadershipId() {
        return leadershipId;
    }

    public void setLeadershipId(Long leadershipId) {
        this.leadershipId = leadershipId;
    }

    public String getLeadershipComment() {
        return leadershipComment;
    }

    public void setLeadershipComment(String leadershipComment) {
        this.leadershipComment = trim(leadershipComment);
    }

    public Long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = trim(degreeName);
    }

    public LocalDate getDegreeStartDate() {
        return degreeStartDate;
    }

    public void setDegreeStartDate(LocalDate degreeStartDate) {
        this.degreeStartDate = degreeStartDate;
    }

    public LocalDate getDegreeEndDate() {
        return degreeEndDate;
    }

    public void setDegreeEndDate(LocalDate degreeEndDate) {
        this.degreeEndDate = degreeEndDate;
    }

    public String getDegreeInstitution() {
        return degreeInstitution;
    }

    public void setDegreeInstitution(String degreeInstitution) {
        this.degreeInstitution = trim(degreeInstitution);
    }

    public Boolean getDegreeCompleted() {
        return degreeCompleted;
    }

    public void setDegreeCompleted(Boolean degreeCompleted) {
        this.degreeCompleted = degreeCompleted;
    }

    public String getDegreeComment() {
        return degreeComment;
    }

    public void setDegreeComment(String degreeComment) {
        this.degreeComment = trim(degreeComment);
    }

    public Long getLeadershipTypeId() {
        return leadershipTypeId;
    }

    public void setLeadershipTypeId(Long leadershipTypeId) {
        this.leadershipTypeId = leadershipTypeId;
    }

    public String getLeadershipTypeName() {
        return leadershipTypeName;
    }

    public void setLeadershipTypeName(String leadershipTypeName) {
        this.leadershipTypeName = trim(leadershipTypeName);
    }

    public CertificateKind getLeadershipKind() {
        return leadershipKind;
    }

    public void setLeadershipKind(CertificateKind leadershipKind) {
        this.leadershipKind = leadershipKind;
    }

    public BigDecimal getLeadershipTypePoints() {
        return leadershipTypePoints;
    }

    public void setLeadershipTypePoints(BigDecimal leadershipTypePoints) {
        this.leadershipTypePoints = leadershipTypePoints;
    }

    public String getLeadershipTypeComment() {
        return leadershipTypeComment;
    }

    public void setLeadershipTypeComment(String leadershipTypeComment) {
        this.leadershipTypeComment = trim(leadershipTypeComment);
    }

    public Long getDegreeTypeId() {
        return degreeTypeId;
    }

    public void setDegreeTypeId(Long degreeTypeId) {
        this.degreeTypeId = degreeTypeId;
    }

    public String getDegreeTypeName() {
        return degreeTypeName;
    }

    public void setDegreeTypeName(String degreeTypeName) {
        this.degreeTypeName = trim(degreeTypeName);
    }

    public BigDecimal getDegreeHighlyRelevantPoints() {
        return degreeHighlyRelevantPoints;
    }

    public void setDegreeHighlyRelevantPoints(BigDecimal degreeHighlyRelevantPoints) {
        this.degreeHighlyRelevantPoints = degreeHighlyRelevantPoints;
    }

    public BigDecimal getDegreeLimitedRelevantPoints() {
        return degreeLimitedRelevantPoints;
    }

    public void setDegreeLimitedRelevantPoints(BigDecimal degreeLimitedRelevantPoints) {
        this.degreeLimitedRelevantPoints = degreeLimitedRelevantPoints;
    }

    public BigDecimal getDegreeLittleRelevantPoints() {
        return degreeLittleRelevantPoints;
    }

    public void setDegreeLittleRelevantPoints(BigDecimal degreeLittleRelevantPoints) {
        this.degreeLittleRelevantPoints = degreeLittleRelevantPoints;
    }

    public Long getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(Long experienceId) {
        this.experienceId = experienceId;
    }

    public String getExperienceName() {
        return experienceName;
    }

    public void setExperienceName(String experienceName) {
        this.experienceName = trim(experienceName);
    }

    public String getExperienceEmployer() {
        return experienceEmployer;
    }

    public void setExperienceEmployer(String experienceEmployer) {
        this.experienceEmployer = trim(experienceEmployer);
    }

    public LocalDate getExperienceStartDate() {
        return experienceStartDate;
    }

    public void setExperienceStartDate(LocalDate experienceStartDate) {
        this.experienceStartDate = experienceStartDate;
    }

    public LocalDate getExperienceEndDate() {
        return experienceEndDate;
    }

    public void setExperienceEndDate(LocalDate experienceEndDate) {
        this.experienceEndDate = experienceEndDate;
    }

    public Integer getExperiencePercent() {
        return experiencePercent;
    }

    public void setExperiencePercent(Integer experiencePercent) {
        this.experiencePercent = experiencePercent;
    }

    public String getExperienceComment() {
        return experienceComment;
    }

    public void setExperienceComment(String experienceComment) {
        this.experienceComment = trim(experienceComment);
    }

    public Long getExperienceTypeId() {
        return experienceTypeId;
    }

    public void setExperienceTypeId(Long experienceTypeId) {
        this.experienceTypeId = experienceTypeId;
    }

    public String getExperienceTypeName() {
        return experienceTypeName;
    }

    public void setExperienceTypeName(String experienceTypeName) {
        this.experienceTypeName = trim(experienceTypeName);
    }

    public BigDecimal getExperienceHighlyRelevantPoints() {
        return experienceHighlyRelevantPoints;
    }

    public void setExperienceHighlyRelevantPoints(BigDecimal experienceHighlyRelevantPoints) {
        this.experienceHighlyRelevantPoints = experienceHighlyRelevantPoints;
    }

    public BigDecimal getExperienceLimitedRelevantPoints() {
        return experienceLimitedRelevantPoints;
    }

    public void setExperienceLimitedRelevantPoints(BigDecimal experienceLimitedRelevantPoints) {
        this.experienceLimitedRelevantPoints = experienceLimitedRelevantPoints;
    }

    public BigDecimal getExperienceLittleRelevantPoints() {
        return experienceLittleRelevantPoints;
    }

    public void setExperienceLittleRelevantPoints(BigDecimal experienceLittleRelevantPoints) {
        this.experienceLittleRelevantPoints = experienceLittleRelevantPoints;
    }

    public Long getCalculationId() {
        return calculationId;
    }

    public void setCalculationId(Long calculationId) {
        this.calculationId = calculationId;
    }

    public CalculationState getCalculationState() {
        return calculationState;
    }

    public void setCalculationState(CalculationState calculationState) {
        this.calculationState = calculationState;
    }

    public LocalDate getCalculationPublicationDate() {
        return calculationPublicationDate;
    }

    public void setCalculationPublicationDate(LocalDate calculationPublicationDate) {
        this.calculationPublicationDate = calculationPublicationDate;
    }

    public String getCalculationPublicizedBy() {
        return calculationPublicizedBy;
    }

    public void setCalculationPublicizedBy(String calculationPublicizedBy) {
        this.calculationPublicizedBy = trim(calculationPublicizedBy);
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = trim(roleName);
    }

    public Boolean getRoleIsManagement() {
        return roleIsManagement;
    }

    public void setRoleIsManagement(Boolean roleIsManagement) {
        this.roleIsManagement = roleIsManagement;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static final class Builder {
        private Long uniqueRowId;
        private Long memberId;
        private String firstName;
        private String lastName;
        private String abbreviation;
        private LocalDate birthDate;
        private LocalDate dateOfHire;
        private EmploymentState employmentState;

        private Long organisationUnitId;
        private String organisationUnitName;

        private Long certificateId;
        private LocalDate certificateCompletedAt;
        private LocalDate certificateValidUntil;
        private String certificateComment;

        private Long certificateTypeId;
        private String certificateTypeName;
        private BigDecimal certificateTypePoints;
        private String certificateTypeComment;

        private Long tagId;
        private String tagName;

        private Long leadershipId;
        private String leadershipComment;

        private Long degreeId;
        private String degreeName;
        private LocalDate degreeStartDate;
        private LocalDate degreeEndDate;
        private String degreeInstitution;
        private Boolean degreeCompleted;
        private String degreeComment;

        private Long leadershipTypeId;
        private String leadershipTypeName;
        private CertificateKind leadershipKind;
        private BigDecimal leadershipTypePoints;
        private String leadershipTypeComment;

        private Long degreeTypeId;
        private String degreeTypeName;
        private BigDecimal degreeHighlyRelevantPoints;
        private BigDecimal degreeLimitedRelevantPoints;
        private BigDecimal degreeLittleRelevantPoints;

        private Long experienceId;
        private String experienceName;
        private String experienceEmployer;
        private LocalDate experienceStartDate;
        private LocalDate experienceEndDate;
        private Integer experiencePercent;
        private String experienceComment;

        private Long experienceTypeId;
        private String experienceTypeName;
        private BigDecimal experienceHighlyRelevantPoints;
        private BigDecimal experienceLimitedRelevantPoints;
        private BigDecimal experienceLittleRelevantPoints;

        private Long calculationId;
        private CalculationState calculationState;
        private LocalDate calculationPublicationDate;
        private String calculationPublicizedBy;

        private Long roleId;
        private String roleName;
        private Boolean roleIsManagement;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withUniqueRowId(Long uniqueRowId) {
            this.uniqueRowId = uniqueRowId;
            return this;
        }

        public Builder withMemberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = trim(firstName);
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = trim(lastName);
            return this;
        }

        public Builder withAbbreviation(String abbreviation) {
            this.abbreviation = trim(abbreviation);
            return this;
        }

        public Builder withBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder withDateOfHire(LocalDate dateOfHire) {
            this.dateOfHire = dateOfHire;
            return this;
        }

        public Builder withEmploymentState(EmploymentState employmentState) {
            this.employmentState = employmentState;
            return this;
        }

        public Builder withOrganisationUnitId(Long organisationUnitId) {
            this.organisationUnitId = organisationUnitId;
            return this;
        }

        public Builder withOrganisationUnitName(String organisationUnitName) {
            this.organisationUnitName = trim(organisationUnitName);
            return this;
        }

        public Builder withCertificateId(Long certificateId) {
            this.certificateId = certificateId;
            return this;
        }

        public Builder withCertificateCompletedAt(LocalDate certificateCompletedAt) {
            this.certificateCompletedAt = certificateCompletedAt;
            return this;
        }

        public Builder withCertificateValidUntil(LocalDate certificateValidUntil) {
            this.certificateValidUntil = certificateValidUntil;
            return this;
        }

        public Builder withCertificateComment(String certificateComment) {
            this.certificateComment = trim(certificateComment);
            return this;
        }

        public Builder withCertificateTypeId(Long certificateTypeId) {
            this.certificateTypeId = certificateTypeId;
            return this;
        }

        public Builder withCertificateTypeName(String certificateTypeName) {
            this.certificateTypeName = trim(certificateTypeName);
            return this;
        }

        public Builder withCertificateTypePoints(BigDecimal certificateTypePoints) {
            this.certificateTypePoints = certificateTypePoints;
            return this;
        }

        public Builder withCertificateTypeComment(String certificateTypeComment) {
            this.certificateTypeComment = trim(certificateTypeComment);
            return this;
        }

        public Builder withTagId(Long tagId) {
            this.tagId = tagId;
            return this;
        }

        public Builder withTagName(String tagName) {
            this.tagName = trim(tagName);
            return this;
        }

        public Builder withLeadershipId(Long leadershipId) {
            this.leadershipId = leadershipId;
            return this;
        }

        public Builder withLeadershipComment(String leadershipComment) {
            this.leadershipComment = trim(leadershipComment);
            return this;
        }

        public Builder withLeadershipTypeId(Long leadershipTypeId) {
            this.leadershipTypeId = leadershipTypeId;
            return this;
        }

        public Builder withLeadershipTypeName(String leadershipTypeName) {
            this.leadershipTypeName = trim(leadershipTypeName);
            return this;
        }

        public Builder withLeadershipKind(CertificateKind leadershipKind) {
            this.leadershipKind = leadershipKind;
            return this;
        }

        public Builder withLeadershipTypePoints(BigDecimal leadershipTypePoints) {
            this.leadershipTypePoints = leadershipTypePoints;
            return this;
        }

        public Builder withLeadershipTypeComment(String leadershipTypeComment) {
            this.leadershipTypeComment = trim(leadershipTypeComment);
            return this;
        }

        public Builder withDegreeId(Long degreeId) {
            this.degreeId = degreeId;
            return this;
        }

        public Builder withDegreeName(String degreeName) {
            this.degreeName = trim(degreeName);
            return this;
        }

        public Builder withDegreeStartDate(LocalDate degreeStartDate) {
            this.degreeStartDate = degreeStartDate;
            return this;
        }

        public Builder withDegreeEndDate(LocalDate degreeEndDate) {
            this.degreeEndDate = degreeEndDate;
            return this;
        }

        public Builder withDegreeInstitution(String degreeInstitution) {
            this.degreeInstitution = trim(degreeInstitution);
            return this;
        }

        public Builder withDegreeCompleted(Boolean degreeCompleted) {
            this.degreeCompleted = degreeCompleted;
            return this;
        }

        public Builder withDegreeComment(String degreeComment) {
            this.degreeComment = trim(degreeComment);
            return this;
        }

        public Builder withDegreeTypeId(Long degreeTypeId) {
            this.degreeTypeId = degreeTypeId;
            return this;
        }

        public Builder withDegreeTypeName(String degreeTypeName) {
            this.degreeTypeName = trim(degreeTypeName);
            return this;
        }

        public Builder withDegreeHighlyRelevantPoints(BigDecimal degreeHighlyRelevantPoints) {
            this.degreeHighlyRelevantPoints = degreeHighlyRelevantPoints;
            return this;
        }

        public Builder withDegreeLimitedRelevantPoints(BigDecimal degreeLimitedRelevantPoints) {
            this.degreeLimitedRelevantPoints = degreeLimitedRelevantPoints;
            return this;
        }

        public Builder withDegreeLittleRelevantPoints(BigDecimal degreeLittleRelevantPoints) {
            this.degreeLittleRelevantPoints = degreeLittleRelevantPoints;
            return this;
        }

        public Builder withExperienceId(Long experienceId) {
            this.experienceId = experienceId;
            return this;
        }

        public Builder withExperienceName(String experienceName) {
            this.experienceName = trim(experienceName);
            return this;
        }

        public Builder withExperienceEmployer(String experienceEmployer) {
            this.experienceEmployer = trim(experienceEmployer);
            return this;
        }

        public Builder withExperienceStartDate(LocalDate experienceStartDate) {
            this.experienceStartDate = experienceStartDate;
            return this;
        }

        public Builder withExperienceEndDate(LocalDate experienceEndDate) {
            this.experienceEndDate = experienceEndDate;
            return this;
        }

        public Builder withExperiencePercent(Integer experiencePercent) {
            this.experiencePercent = experiencePercent;
            return this;
        }

        public Builder withExperienceComment(String experienceComment) {
            this.experienceComment = trim(experienceComment);
            return this;
        }

        public Builder withExperienceTypeId(Long experienceTypeId) {
            this.experienceTypeId = experienceTypeId;
            return this;
        }

        public Builder withExperienceTypeName(String experienceTypeName) {
            this.experienceTypeName = trim(experienceTypeName);
            return this;
        }

        public Builder withExperienceHighlyRelevantPoints(BigDecimal experienceHighlyRelevantPoints) {
            this.experienceHighlyRelevantPoints = experienceHighlyRelevantPoints;
            return this;
        }

        public Builder withExperienceLimitedRelevantPoints(BigDecimal experienceLimitedRelevantPoints) {
            this.experienceLimitedRelevantPoints = experienceLimitedRelevantPoints;
            return this;
        }

        public Builder withExperienceLittleRelevantPoints(BigDecimal experienceLittleRelevantPoints) {
            this.experienceLittleRelevantPoints = experienceLittleRelevantPoints;
            return this;
        }

        public Builder withCalculationId(Long calculationId) {
            this.calculationId = calculationId;
            return this;
        }

        public Builder withCalculationState(CalculationState calculationState) {
            this.calculationState = calculationState;
            return this;
        }

        public Builder withCalculationPublicationDate(LocalDate calculationPublicationDate) {
            this.calculationPublicationDate = calculationPublicationDate;
            return this;
        }

        public Builder withCalculationPublicizedBy(String calculationPublicizedBy) {
            this.calculationPublicizedBy = trim(calculationPublicizedBy);
            return this;
        }

        public Builder withRoleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder withRoleName(String roleName) {
            this.roleName = trim(roleName);
            return this;
        }

        public Builder withRoleIsManagement(Boolean roleIsManagement) {
            this.roleIsManagement = roleIsManagement;
            return this;
        }
    }
}