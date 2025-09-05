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

    private LocalDateTime deleted_at;

    public Role(Long id, String name, LocalDateTime deleted_at) {
        this.id = id;
        this.name = name;
        this.deleted_at = deleted_at;
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

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }
}
