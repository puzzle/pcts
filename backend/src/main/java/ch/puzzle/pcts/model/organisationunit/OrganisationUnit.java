package ch.puzzle.pcts.model.organisationunit;

import static org.apache.commons.lang3.StringUtils.trim;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE organisation_unit SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class OrganisationUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public OrganisationUnit(Long id, String name) {
        this.id = id;
        this.name = trim(name);
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
        this.name = trim(name);
    }

    @Override
    public String toString() {
        return "OrganisationUnit{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
