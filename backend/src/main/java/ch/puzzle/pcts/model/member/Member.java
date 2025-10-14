package ch.puzzle.pcts.model.member;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import jakarta.persistence.*;
import java.util.Date;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private EmploymentState employmentState;

    private String abbreviation;

    private Date dateOfHire;

    private Date birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_unit")
    private OrganisationUnit organisationUnit;

    private Member(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.lastName = builder.lastName;
        this.employmentState = builder.employmentState;
        this.abbreviation = builder.abbreviation;
        this.dateOfHire = builder.dateOfHire;
        this.birthDate = builder.birthDate;
        this.organisationUnit = builder.organisationUnit;
    }

    public Member() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
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
        this.abbreviation = abbreviation == null ? null : abbreviation.trim();
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

    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", name='" + name + '\'' + ", lastName='" + lastName + '\''
               + ", employmentState=" + employmentState + ", abbreviation='" + abbreviation + '\'' + ", dateOfHire="
               + dateOfHire + ", birthDate=" + birthDate + ", organisationUnit=" + organisationUnit + '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
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

        public Builder withName(String name) {
            this.name = name == null ? null : name.trim();
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName == null ? null : lastName.trim();
            return this;
        }

        public Builder withEmploymentState(EmploymentState employmentState) {
            this.employmentState = employmentState;
            return this;
        }

        public Builder withAbbreviation(String abbreviation) {
            this.abbreviation = abbreviation == null ? null : abbreviation.trim();
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
