package ch.puzzle.pcts.model.degreetype;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.util.PCTSPointsValidation;
import ch.puzzle.pcts.util.PCTSStringValidation;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
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
    private Timestamp deletedAt;

    public DegreeType(Long id, String name, BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
                      BigDecimal littleRelevantPoints) {
        this.id = id;
        this.name = trim(name);
        this.highlyRelevantPoints = highlyRelevantPoints;
        this.limitedRelevantPoints = limitedRelevantPoints;
        this.littleRelevantPoints = littleRelevantPoints;
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

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
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
}
