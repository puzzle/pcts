package ch.puzzle.pcts.model.member;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Member implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PCTSStringValidation
    private String firstName;

    @PCTSStringValidation
    private String lastName;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{attribute.not.null}")
    private EmploymentState employmentState;

    private String abbreviation;

    private LocalDate dateOfHire;

    @NotNull(message = "{attribute.not.null}")
    @Past(message = "{attribute.date.past}")
    private LocalDate birthDate;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_unit")
    private OrganisationUnit organisationUnit;

    @Min(value = 1, message = "{attribute.min.value}")
    private Long ptimeId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Past(message = "{attribute.date.past}")
    private LocalDateTime lastSuccessfulSync;

    @Min(value = 0, message = "{attribute.not.negative}")
    private Integer syncErrorCount;

    private Member(Builder builder) {
        this.id = builder.id;
        this.firstName = trim(builder.firstName);
        this.lastName = trim(builder.lastName);
        this.employmentState = builder.employmentState;
        this.abbreviation = trim(builder.abbreviation);
        this.dateOfHire = builder.dateOfHire;
        this.birthDate = builder.birthDate;
        this.organisationUnit = builder.organisationUnit;
        this.ptimeId = builder.ptimeId;
        this.lastSuccessfulSync = builder.lastSuccessfulSync;
        this.syncErrorCount = builder.syncErrorCount;
        this.deletedAt = null;
    }

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public EmploymentState getEmploymentState() {
        return employmentState;
    }

    public void setEmploymentState(EmploymentState employmentState) {
        this.employmentState = employmentState;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = trim(abbreviation);
    }

    public LocalDate getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(LocalDate dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public OrganisationUnit getOrganisationUnit() {
        return organisationUnit;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getPtimeId() {
        return ptimeId;
    }

    public void setPtimeId(Long ptimeId) {
        this.ptimeId = ptimeId;
    }

    public LocalDateTime getLastSuccessfulSync() {
        return lastSuccessfulSync;
    }

    public void setLastSuccessfulSync(LocalDateTime lastSuccessfulSync) {
        this.lastSuccessfulSync = lastSuccessfulSync;
    }

    public Integer getSyncErrorCount() {
        return syncErrorCount;
    }

    public void setSyncErrorCount(Integer syncErrorCount) {
        this.syncErrorCount = syncErrorCount;
    }

    public void keepSyncData(Long ptimeId, LocalDateTime lastSuccessfulSync, Integer syncErrorCount) {
        this.ptimeId = ptimeId;
        this.lastSuccessfulSync = lastSuccessfulSync;
        this.syncErrorCount = syncErrorCount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Member member))
            return false;
        return Objects.equals(getId(), member.getId()) && Objects.equals(getFirstName(), member.getFirstName())
               && Objects.equals(getLastName(), member.getLastName())
               && getEmploymentState() == member.getEmploymentState()
               && Objects.equals(getAbbreviation(), member.getAbbreviation())
               && Objects.equals(getDateOfHire(), member.getDateOfHire())
               && Objects.equals(getBirthDate(), member.getBirthDate())
               && Objects.equals(getDeletedAt(), member.getDeletedAt())
               && Objects.equals(getOrganisationUnit(), member.getOrganisationUnit())
               && Objects.equals(getPtimeId(), member.getPtimeId())
               && Objects.equals(getLastSuccessfulSync(), member.getLastSuccessfulSync())
               && Objects.equals(getSyncErrorCount(), member.getSyncErrorCount());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getFirstName(),
                      getLastName(),
                      getEmploymentState(),
                      getAbbreviation(),
                      getDateOfHire(),
                      getBirthDate(),
                      getDeletedAt(),
                      getOrganisationUnit(),
                      getPtimeId(),
                      getLastSuccessfulSync(),
                      getSyncErrorCount());
    }

    @Override
    public String toString() {
        return "Member{" + "id=" + getId() + ", firstName='" + getFirstName() + '\'' + ", lastName='" + getLastName()
               + '\'' + ", employmentState=" + getEmploymentState() + ", abbreviation='" + getAbbreviation() + '\''
               + ", dateOfHire=" + getDateOfHire() + ", birthDate=" + getBirthDate() + ", deletedAt=" + getDeletedAt()
               + ", organisationUnit=" + getOrganisationUnit() + ", ptimeId=" + getPtimeId() + ", lastSuccessfulSync="
               + getLastSuccessfulSync() + ", syncErrorCount=" + getSyncErrorCount() + '}';
    }

    public static final class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private EmploymentState employmentState;
        private String abbreviation;
        private LocalDate dateOfHire;
        private LocalDate birthDate;
        private OrganisationUnit organisationUnit;
        private Long ptimeId;
        private LocalDateTime lastSuccessfulSync;
        private Integer syncErrorCount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
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

        public Builder withEmploymentState(EmploymentState employmentState) {
            this.employmentState = employmentState;
            return this;
        }

        public Builder withAbbreviation(String abbreviation) {
            this.abbreviation = trim(abbreviation);
            return this;
        }

        public Builder withDateOfHire(LocalDate dateOfHire) {
            this.dateOfHire = dateOfHire;
            return this;
        }

        public Builder withBirthDate(LocalDate birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder withOrganisationUnit(OrganisationUnit organisationUnit) {
            this.organisationUnit = organisationUnit;
            return this;
        }

        public Builder withPtimeId(Long ptimeId) {
            this.ptimeId = ptimeId;
            return this;
        }

        public Builder withLastSuccessfulSync(LocalDateTime lastSuccessfulSync) {
            this.lastSuccessfulSync = lastSuccessfulSync;
            return this;
        }

        public Builder withSyncErrorCount(Integer syncErrorCount) {
            this.syncErrorCount = syncErrorCount;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }
}
