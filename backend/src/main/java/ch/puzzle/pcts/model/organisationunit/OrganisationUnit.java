package ch.puzzle.pcts.model.organisationunit;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE organisation_unit SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class OrganisationUnit implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public OrganisationUnit(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public OrganisationUnit() {
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

    @Override
    public String toString() {
        return "OrganisationUnit{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
