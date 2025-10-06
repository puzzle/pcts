package ch.puzzle.pcts.model.member;

import ch.puzzle.pcts.model.organisationunit.OrganisationUnit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "{ATTRIBUTE_NOT_BLANK}")
    @NotNull(message = "{ATTRIBUTE_NOT_NULL}")
    @Size(min = 2, max = 250, message = "{ATTRIBUTE_SIZE_BETWEEN}")
    private String name;

    @NotBlank(message = "{attribute.not.blank}")
    @NotNull(message = "{attribute.notnull}")
    @Size(min = 2, max = 250, message = "{attribute.size.between}")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private EmploymentState employmentState;

    @NotBlank(message = "{ATTRIBUTE_NOT_BLANK}")
    @NotNull(message = "{ATTRIBUTE_NOT_NULL}")
    @Size(min = 2, max = 250, message = "{ATTRIBUTE_SIZE_BETWEEN}")
    private String abbreviation;

    // @NotBlank(message = "{ATTRIBUTE_NOT_BLANK}")
    @NotNull(message = "{ATTRIBUTE_NOT_NULL}")
    private Date dateOfHire;

    @NotNull(message = "{ATTRIBUTE_NOT_NULL}")
    private boolean isAdmin;

    // @NotBlank(message = "{ATTRIBUTE_NOT_BLANK}")
    @NotNull(message = "{ATTRIBUTE_NOT_NULL}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_unit")
    private OrganisationUnit organisationUnit;

    public Member() {
    }

    public Member(Long id, String name, String lastName, EmploymentState employmentState, String abbreviation,
                  Date dateOfHire, boolean isAdmin, OrganisationUnit organisationUnit) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.employmentState = employmentState;
        this.abbreviation = abbreviation;
        this.dateOfHire = dateOfHire;
        this.isAdmin = isAdmin;
        this.organisationUnit = organisationUnit;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public EmploymentState getEmploymentState() {
        return employmentState;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public Date getDateOfHire() {
        return dateOfHire;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public OrganisationUnit getOrganisationUnit() {
        return organisationUnit;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmploymentState(EmploymentState employmentState) {
        this.employmentState = employmentState;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setDateOfHire(Date dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }
}
