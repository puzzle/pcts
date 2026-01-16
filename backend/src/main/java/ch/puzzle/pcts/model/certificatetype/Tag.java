package ch.puzzle.pcts.model.certificatetype;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Tag implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PCTSStringValidation
    private String name;

    private Tag(Builder builder) {
        this.id = builder.id;
        this.name = trim(builder.name);
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

    public static final class Builder {
        private Long id;
        private String name;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Tag build() {
            return new Tag(this);
        }
    }
}
