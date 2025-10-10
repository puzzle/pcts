package ch.puzzle.pcts.model.role;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE role SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Role implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isManagement;

    public Role(Long id, String name, boolean isManagement) {
        this.id = id;
        this.name = name;
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
        this.name = name;
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
