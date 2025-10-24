package ch.puzzle.pcts.model.certificate;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Tag implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    public Tag(Long id, String name) {
        this.id = id;
        this.name = trim(name);
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
        this.name = trim(name);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tag tag))
            return false;
        return Objects.equals(getId(), tag.getId()) && Objects.equals(getName(), tag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Tag{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
