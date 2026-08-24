package ch.puzzle.pcts.model.role;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.validation.PCTSStringValidation;
import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime deletedAt;

    private Role(Builder builder) {
        this.id = builder.id;
        this.name = trim(builder.name);
        this.isManagement = builder.isManagement;
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

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Role role)) {
            return false;
        }
        return getIsManagement() == role.getIsManagement() && Objects.equals(getId(), role.getId())
               && Objects.equals(getName(), role.getName()) && Objects.equals(getDeletedAt(), role.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getIsManagement(), getDeletedAt());
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + id + ", name='" + name + '\'' + ", isManagement=" + isManagement + ", deletedAt="
               + deletedAt + '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private boolean isManagement;

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

        public Builder withIsManagement(boolean isManagement) {
            this.isManagement = isManagement;
            return this;
        }

        public Role build() {
            return new Role(this);
        }
    }
}
