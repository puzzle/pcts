package ch.puzzle.pcts.model.degreetype;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.util.PCTSPointsValidation;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE degree_type SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class DegreeType implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PCTSStringValidation
    private String name;

    @PCTSPointsValidation
    private BigDecimal highlyRelevantPoints;

    @PCTSPointsValidation
    private BigDecimal limitedRelevantPoints;

    @PCTSPointsValidation
    private BigDecimal littleRelevantPoints;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private LocalDateTime deletedAt;

    private DegreeType(Builder builder) {
        this.id = builder.id;
        this.name = trim(builder.name);
        this.highlyRelevantPoints = builder.highlyRelevantPoints;
        this.limitedRelevantPoints = builder.limitedRelevantPoints;
        this.littleRelevantPoints = builder.littleRelevantPoints;
    }

    public DegreeType() {
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

    public BigDecimal getPointsByRelevancy(Relevancy relevancy) {
        return switch (relevancy) {
            case HIGHLY -> getHighlyRelevantPoints();
            case LIMITED -> getLimitedRelevantPoints();
            case LITTLE -> getLittleRelevantPoints();
            case NONE -> BigDecimal.ZERO;
        };
    }

    public BigDecimal getHighlyRelevantPoints() {
        return highlyRelevantPoints;
    }

    public void setHighlyRelevantPoints(BigDecimal highlyRelevantPoints) {
        this.highlyRelevantPoints = highlyRelevantPoints;
    }

    public BigDecimal getLimitedRelevantPoints() {
        return limitedRelevantPoints;
    }

    public void setLimitedRelevantPoints(BigDecimal limitedRelevantPoints) {
        this.limitedRelevantPoints = limitedRelevantPoints;
    }

    public BigDecimal getLittleRelevantPoints() {
        return littleRelevantPoints;
    }

    public void setLittleRelevantPoints(BigDecimal littleRelevantPoints) {
        this.littleRelevantPoints = littleRelevantPoints;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DegreeType that))
            return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName())
               && Objects.equals(getHighlyRelevantPoints(), that.getHighlyRelevantPoints())
               && Objects.equals(getLimitedRelevantPoints(), that.getLimitedRelevantPoints())
               && Objects.equals(getLittleRelevantPoints(), that.getLittleRelevantPoints())
               && Objects.equals(getDeletedAt(), that.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getName(),
                      getHighlyRelevantPoints(),
                      getLimitedRelevantPoints(),
                      getLittleRelevantPoints(),
                      getDeletedAt());
    }

    @Override
    public String toString() {
        return "DegreeType{" + "id=" + id + ", name='" + name + '\'' + ", highlyRelevantPoints=" + highlyRelevantPoints
               + ", limitedRelevantPoints=" + limitedRelevantPoints + ", littleRelevantPoints=" + littleRelevantPoints
               + ", deletedAt=" + deletedAt + '}';
    }

    public static final class Builder {
        private Long id;
        private String name;
        private BigDecimal highlyRelevantPoints;
        private BigDecimal limitedRelevantPoints;
        private BigDecimal littleRelevantPoints;

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

        public Builder withHighlyRelevantPoints(BigDecimal highlyRelevantPoints) {
            this.highlyRelevantPoints = highlyRelevantPoints;
            return this;
        }

        public Builder withLimitedRelevantPoints(BigDecimal limitedRelevantPoints) {
            this.limitedRelevantPoints = limitedRelevantPoints;
            return this;
        }

        public Builder withLittleRelevantPoints(BigDecimal littleRelevantPoints) {
            this.littleRelevantPoints = littleRelevantPoints;
            return this;
        }

        public DegreeType build() {
            return new DegreeType(this);
        }
    }
}
