package ch.puzzle.pcts.model.organisationunit;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE organisation_unit SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
// @SQLRestriction("deleted_at IS NULL")
public class OrganisationUnit implements Model {
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
    public boolean equals(Object o) {
        if (!(o instanceof OrganisationUnit organisationUnit))
            return false;
        return Objects.equals(id, organisationUnit.id) && Objects.equals(name, organisationUnit.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "OrganisationUnit{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
