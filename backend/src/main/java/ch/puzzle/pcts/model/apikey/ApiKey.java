package ch.puzzle.pcts.model.apikey;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.validation.PCTSStringValidation;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "api_key")
public class ApiKey implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PCTSStringValidation
    private String name;

    @PCTSStringValidation
    private String hashedKey;

    private LocalDateTime lastUsed;

    private boolean revoked;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = trim(name);
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = trim(hashedKey);
    }

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ApiKey apiKey)) {
            return false;
        }
        return isRevoked() == apiKey.isRevoked() && Objects.equals(getId(), apiKey.getId())
               && Objects.equals(getName(), apiKey.getName()) && Objects.equals(getHashedKey(), apiKey.getHashedKey())
               && Objects.equals(getLastUsed(), apiKey.getLastUsed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getHashedKey(), getLastUsed(), isRevoked());
    }

    @Override
    public String toString() {
        return "ApiKey{" + "id=" + id + ", name='" + name + '\'' + ", lastUsed=" + lastUsed + ", revoked=" + revoked
               + '}';
    }
}
