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
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "member_id_seq")
    @SequenceGenerator(name = "member_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private EmploymentState employmentState;

    private String abbreviation;

    private Date dateOfHire;

    private Date birthDate;

    private boolean isAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_unit")
    private OrganisationUnit organisationUnit;

    public Member(Long id, String name, String lastName, EmploymentState employmentState, String abbreviation,
                  Date dateOfHire, Date birthDate, boolean isAdmin, OrganisationUnit organisationUnit) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.employmentState = employmentState;
        this.abbreviation = abbreviation;
        this.dateOfHire = dateOfHire;
        this.birthDate = birthDate;
        this.isAdmin = isAdmin;
        this.organisationUnit = organisationUnit;
    }

    public Member() {
        // JPA requires a no-arg constructor
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
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        this.abbreviation = abbreviation;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public OrganisationUnit getOrganisationUnit() {
        return organisationUnit;
    }

    public void setOrganisationUnit(OrganisationUnit organisationUnit) {
        this.organisationUnit = organisationUnit;
    }
}
