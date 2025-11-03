package ch.puzzle.pcts.model.member;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

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

    @PCTSStringValidation
    private String abbreviation;

    private Date dateOfHire;

    @NotNull(message = "{attribute.not.null}")
    @Past(message = "{attribute.date.past}")
    private Date birthDate;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private Timestamp deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_unit")
    private OrganisationUnit organisationUnit;

    private Member(Builder builder) {
        this.id = builder.id;
        this.firstName = trim(builder.firstName);
        this.lastName = trim(builder.lastName);
        this.employmentState = builder.employmentState;
        this.abbreviation = trim(builder.abbreviation);
        this.dateOfHire = builder.dateOfHire;
        this.birthDate = builder.birthDate;
        this.organisationUnit = builder.organisationUnit;
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

    public void setName(String name) {
        this.firstName = trim(name);
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

    public Date getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(Date dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public OrganisationUnit getOrganisationUnit() {
        return organisationUnit;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
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
               && Objects.equals(getOrganisationUnit(), member.getOrganisationUnit());
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
                      getOrganisationUnit());
    }

    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
               + ", employmentState=" + employmentState + ", abbreviation='" + abbreviation + '\'' + ", dateOfHire="
               + dateOfHire + ", birthDate=" + birthDate + ", deletedAt=" + deletedAt + ", organisationUnit="
               + organisationUnit + '}';
    }

    public static final class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private EmploymentState employmentState;
        private String abbreviation;
        private Date dateOfHire;
        private Date birthDate;
        private OrganisationUnit organisationUnit;

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

        public Builder withDateOfHire(Date dateOfHire) {
            this.dateOfHire = dateOfHire;
            return this;
        }

        public Builder withBirthDate(Date birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder withOrganisationUnit(OrganisationUnit organisationUnit) {
            this.organisationUnit = organisationUnit;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }
}
