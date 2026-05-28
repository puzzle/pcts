package ch.puzzle.pcts.model.organisationunit;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.validation.PCTSStringValidation;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE organisation_unit SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class OrganisationUnit implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PCTSStringValidation
    private String name;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

    private OrganisationUnit(Builder builder) {
        this.id = builder.id;
        this.name = trim(builder.name);
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrganisationUnit that))
            return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
               && Objects.equals(getDeletedAt(), that.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDeletedAt());
    }

    @Override
    public String toString() {
        return "OrganisationUnit{" + "id=" + id + ", name='" + name + '\'' + ", deletedAt=" + deletedAt + '}';
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
            this.name = trim(name);
            return this;
        }

        public OrganisationUnit build() {
            return new OrganisationUnit(this);
        }
    }
}
