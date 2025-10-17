package ch.puzzle.pcts.model.certificate;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.validation.basic_string_validation.BasicStringValidation;
import jakarta.persistence.*;

@Entity
public class Tag implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @BasicStringValidation
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
    public String toString() {
        return "Tag{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
