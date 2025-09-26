package ch.puzzle.pcts.model.certificate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag() {
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
        return "Tag{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
