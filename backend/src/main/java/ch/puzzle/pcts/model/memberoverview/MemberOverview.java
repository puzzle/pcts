package ch.puzzle.pcts.model.memberoverview;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.member.EmploymentState;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "member_overview")
@IdClass(MemberOverviewId.class)
public class MemberOverview {

    @Id
    private Long memberId;
    private String firstName;
    private String lastName;
    private String abbreviation;

    private LocalDate birthDate;
    private LocalDate dateOfHire;
    @Enumerated(EnumType.STRING)
    private EmploymentState employmentState;
    private String organisationUnitName;

    @Id
    private Long certificateId;
    private LocalDate certificateCompletedAt;
    private String certificateComment;

    private String certificateTypeName;
    @Enumerated(EnumType.STRING)
    private CertificateKind leadershipTypeKind;

    @Id
    private Long degreeId;
    private String degreeName;
    private LocalDate degreeStartDate;
    private LocalDate degreeEndDate;

    private String degreeTypeName;

    @Id
    private Long experienceId;
    private String experienceName;
    private String experienceEmployer;
    private LocalDate experienceStartDate;
    private LocalDate experienceEndDate;
    private String experienceComment;

    private String experienceTypeName;

    private MemberOverview(Builder builder) {
        memberId = builder.memberId;
        firstName = trim(builder.firstName);
        lastName = trim(builder.lastName);
        abbreviation = trim(builder.abbreviation);
        birthDate = builder.birthDate;
        dateOfHire = builder.dateOfHire;
        employmentState = builder.employmentState;
        organisationUnitName = trim(builder.organisationUnitName);
        certificateId = builder.certificateId;
        certificateCompletedAt = builder.certificateCompletedAt;
        certificateComment = trim(builder.certificateComment);
        certificateTypeName = trim(builder.certificateTypeName);
        degreeId = builder.degreeId;
        degreeName = trim(builder.degreeName);
        degreeStartDate = builder.degreeStartDate;
        degreeEndDate = builder.degreeEndDate;
        leadershipTypeKind = builder.leadershipTypeKind;
        degreeTypeName = trim(builder.degreeTypeName);
        experienceId = builder.experienceId;
        experienceName = trim(builder.experienceName);
        experienceEmployer = trim(builder.experienceEmployer);
        experienceStartDate = builder.experienceStartDate;
        experienceEndDate = builder.experienceEndDate;
        experienceComment = trim(builder.experienceComment);
        experienceTypeName = trim(builder.experienceTypeName);
    }

    public MemberOverview() {

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

    public String getCertificateComment() {
        return certificateComment;
    }

    public void setCertificateComment(String certificateComment) {
        this.certificateComment = trim(certificateComment);
    }

    public String getCertificateTypeName() {
        return certificateTypeName;
    }

    public void setCertificateTypeName(String certificateTypeName) {
        this.certificateTypeName = trim(certificateTypeName);
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

    public CertificateKind getLeadershipTypeKind() {
        return leadershipTypeKind;
    }

    public void setLeadershipTypeKind(CertificateKind leadershipTypeKind) {
        this.leadershipTypeKind = leadershipTypeKind;
    }

    public String getDegreeTypeName() {
        return degreeTypeName;
    }

    public void setDegreeTypeName(String degreeTypeName) {
        this.degreeTypeName = trim(degreeTypeName);
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

    public String getExperienceComment() {
        return experienceComment;
    }

    public void setExperienceComment(String experienceComment) {
        this.experienceComment = trim(experienceComment);
    }

    public String getExperienceTypeName() {
        return experienceTypeName;
    }

    public void setExperienceTypeName(String experienceTypeName) {
        this.experienceTypeName = trim(experienceTypeName);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MemberOverview that))
            return false;
        return Objects.equals(getMemberId(), that.getMemberId()) && Objects.equals(getFirstName(), that.getFirstName())
               && Objects.equals(getLastName(), that.getLastName())
               && Objects.equals(getAbbreviation(), that.getAbbreviation())
               && Objects.equals(getBirthDate(), that.getBirthDate())
               && Objects.equals(getDateOfHire(), that.getDateOfHire())
               && getEmploymentState() == that.getEmploymentState()
               && Objects.equals(getOrganisationUnitName(), that.getOrganisationUnitName())
               && Objects.equals(getCertificateId(), that.getCertificateId())
               && Objects.equals(getCertificateCompletedAt(), that.getCertificateCompletedAt())
               && Objects.equals(getCertificateComment(), that.getCertificateComment())
               && Objects.equals(getCertificateTypeName(), that.getCertificateTypeName())
               && getLeadershipTypeKind() == that.getLeadershipTypeKind()
               && Objects.equals(getDegreeId(), that.getDegreeId())
               && Objects.equals(getDegreeName(), that.getDegreeName())
               && Objects.equals(getDegreeStartDate(), that.getDegreeStartDate())
               && Objects.equals(getDegreeEndDate(), that.getDegreeEndDate())
               && Objects.equals(getDegreeTypeName(), that.getDegreeTypeName())
               && Objects.equals(getExperienceId(), that.getExperienceId())
               && Objects.equals(getExperienceName(), that.getExperienceName())
               && Objects.equals(getExperienceEmployer(), that.getExperienceEmployer())
               && Objects.equals(getExperienceStartDate(), that.getExperienceStartDate())
               && Objects.equals(getExperienceEndDate(), that.getExperienceEndDate())
               && Objects.equals(getExperienceComment(), that.getExperienceComment())
               && Objects.equals(getExperienceTypeName(), that.getExperienceTypeName());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getMemberId(),
                      getFirstName(),
                      getLastName(),
                      getAbbreviation(),
                      getBirthDate(),
                      getDateOfHire(),
                      getEmploymentState(),
                      getOrganisationUnitName(),
                      getCertificateId(),
                      getCertificateCompletedAt(),
                      getCertificateComment(),
                      getCertificateTypeName(),
                      getLeadershipTypeKind(),
                      getDegreeId(),
                      getDegreeName(),
                      getDegreeStartDate(),
                      getDegreeEndDate(),
                      getDegreeTypeName(),
                      getExperienceId(),
                      getExperienceName(),
                      getExperienceEmployer(),
                      getExperienceStartDate(),
                      getExperienceEndDate(),
                      getExperienceComment(),
                      getExperienceTypeName());
    }

    @Override
    public String toString() {
        return "MemberOverview{" + "memberId=" + memberId + ", firstName='" + firstName + '\'' + ", lastName='"
               + lastName + '\'' + ", abbreviation='" + abbreviation + '\'' + ", birthDate=" + birthDate
               + ", dateOfHire=" + dateOfHire + ", employmentState=" + employmentState + ", organisationUnitName='"
               + organisationUnitName + '\'' + ", certificateId=" + certificateId + ", certificateCompletedAt="
               + certificateCompletedAt + ", certificateComment='" + certificateComment + '\''
               + ", certificateTypeName='" + certificateTypeName + '\'' + ", leadershipTypeKind=" + leadershipTypeKind
               + ", degreeId=" + degreeId + ", degreeName='" + degreeName + '\'' + ", degreeStartDate="
               + degreeStartDate + ", degreeEndDate=" + degreeEndDate + ", degreeTypeName='" + degreeTypeName + '\''
               + ", experienceId=" + experienceId + ", experienceName='" + experienceName + '\''
               + ", experienceEmployer='" + experienceEmployer + '\'' + ", experienceStartDate=" + experienceStartDate
               + ", experienceEndDate=" + experienceEndDate + ", experienceComment='" + experienceComment + '\''
               + ", experienceTypeName='" + experienceTypeName + '\'' + '}';
    }

    public static final class Builder {
        private Long memberId;
        private String firstName;
        private String lastName;
        private String abbreviation;
        private LocalDate birthDate;
        private LocalDate dateOfHire;
        private EmploymentState employmentState;

        private String organisationUnitName;

        private Long certificateId;
        private LocalDate certificateCompletedAt;
        private String certificateComment;

        private String certificateTypeName;

        private Long degreeId;
        private String degreeName;
        private LocalDate degreeStartDate;
        private LocalDate degreeEndDate;

        private CertificateKind leadershipTypeKind;

        private String degreeTypeName;

        private Long experienceId;
        private String experienceName;
        private String experienceEmployer;
        private LocalDate experienceStartDate;
        private LocalDate experienceEndDate;
        private String experienceComment;

        private String experienceTypeName;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
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

        public Builder withCertificateComment(String certificateComment) {
            this.certificateComment = trim(certificateComment);
            return this;
        }

        public Builder withCertificateTypeName(String certificateTypeName) {
            this.certificateTypeName = trim(certificateTypeName);
            return this;
        }

        public Builder withleadershipTypeKind(CertificateKind leadershipTypeKind) {
            this.leadershipTypeKind = leadershipTypeKind;
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

        public Builder withDegreeTypeName(String degreeTypeName) {
            this.degreeTypeName = trim(degreeTypeName);
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

        public Builder withExperienceComment(String experienceComment) {
            this.experienceComment = trim(experienceComment);
            return this;
        }

        public Builder withExperienceTypeName(String experienceTypeName) {
            this.experienceTypeName = trim(experienceTypeName);
            return this;
        }

        public MemberOverview build() {
            return new MemberOverview(this);
        }
    }
}