package ch.puzzle.pcts.model.role;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE role SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class Role implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PCTSStringValidation
    @Column(unique = true)
    private String name;

    private boolean isManagement;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private Timestamp deletedAt;

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

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role role))
            return false;
        return isManagement == role.isManagement && Objects.equals(getId(), role.getId())
               && Objects.equals(getName(), role.getName()) && Objects.equals(getDeletedAt(), role.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), isManagement, getDeletedAt());
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + ", isManagement=" + isManagement + ", deletedAt="
               + deletedAt + '}';
    }
}
