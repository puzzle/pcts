package ch.puzzle.pcts.model.role;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE role SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Role implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{attribute.not.blank}")
    @NotNull(message = "{attribute.notnull}")
    @Size(min = 2, max = 250, message = "{attribute.size.between}")
    private String name;

    @NotNull(message = "{attribute.notnull}")
    private boolean isManagement;

    public Role(Long id, String name, boolean isManagement) {
        this.id = id;
        this.name = trim(name);
        this.isManagement = isManagement;
    }

    public Role() {
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

    public boolean getIsManagement() {
        return this.isManagement;
    }

    public void setIsManagement(boolean isManagement) {
        this.isManagement = isManagement;
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + ", isManagement=" + isManagement + '}';
    }
}
