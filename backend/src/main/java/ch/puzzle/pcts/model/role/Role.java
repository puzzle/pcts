package ch.puzzle.pcts.model.role;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_role")
    @SequenceGenerator(name = "sequence_role", allocationSize = 1)
    private Long id;

    private String name;

    private LocalDateTime deletedAt;

    private boolean isManagement;

    public Role(Long id, String name, LocalDateTime deletedAt, boolean isManagement) {
        this.id = id;
        this.name = name;
        this.deletedAt = deletedAt;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean getIsManagement() {
        return this.isManagement;
    }

    public void setIsManagement(boolean isManagement) {
        this.isManagement = isManagement;
    }
}
