package ch.puzzle.pcts.model.member;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import ch.puzzle.pcts.util.validation.PCTSStringValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private String abbreviation;

    private LocalDate dateOfHire;

    @Email(message = "{attribute.not.email}")
    private String email;

    @NotNull(message = "{attribute.not.null}")
    @Past(message = "{attribute.date.past}")
    private LocalDate birthDate;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

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
        this.email = builder.email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = trim(email);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Member member)) {
            return false;
        }
        return Objects.equals(getId(), member.getId()) && Objects.equals(getFirstName(), member.getFirstName())
               && Objects.equals(getLastName(), member.getLastName())
               && getEmploymentState() == member.getEmploymentState()
               && Objects.equals(getAbbreviation(), member.getAbbreviation())
               && Objects.equals(getDateOfHire(), member.getDateOfHire())
               && Objects.equals(getEmail(), member.getEmail()) && Objects.equals(getBirthDate(), member.getBirthDate())
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
                      getEmail(),
                      getBirthDate(),
                      getDeletedAt(),
                      getOrganisationUnit());
    }

    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
               + ", employmentState=" + employmentState + ", abbreviation='" + abbreviation + '\'' + ", dateOfHire="
               + dateOfHire + ", email='" + email + '\'' + ", birthDate=" + birthDate + ", deletedAt=" + deletedAt
               + ", organisationUnit=" + organisationUnit + '}';
    }

    public static final class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private EmploymentState employmentState;
        private String abbreviation;
        private LocalDate dateOfHire;
        private LocalDate birthDate;
        private String email;
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

        public Builder withEmail(String email) {
            this.email = trim(email);
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }
}
