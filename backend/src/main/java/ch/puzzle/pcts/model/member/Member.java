package ch.puzzle.pcts.model.member;

import ch.puzzle.pcts.model.organisation_unit.OrganisationUnit;
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

    private boolean abbreviation;

    private Date dateOfHire;

    private boolean isAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_unit")
    private OrganisationUnit organisationUnit;
}
