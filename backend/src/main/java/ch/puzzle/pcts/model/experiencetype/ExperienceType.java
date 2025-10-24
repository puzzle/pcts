package ch.puzzle.pcts.model.experiencetype;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE experience_type SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
public class ExperienceType implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal highlyRelevantPoints;

    private BigDecimal limitedRelevantPoints;

    private BigDecimal littleRelevantPoints;

    @Column(name = "deleted_at", insertable = false, updatable = false)
    private Timestamp deletedAt;

    public ExperienceType(Long id, String name, BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
                          BigDecimal littleRelevantPoints) {
        this.id = id;
        this.name = trim(name);
        this.highlyRelevantPoints = highlyRelevantPoints;
        this.limitedRelevantPoints = limitedRelevantPoints;
        this.littleRelevantPoints = littleRelevantPoints;
    }

    public ExperienceType() {
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

    public Timestamp getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }

    public void setLittleRelevantPoints(BigDecimal littleRelevantPoints) {
        this.littleRelevantPoints = littleRelevantPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ExperienceType that))
            return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
               && Objects.equals(highlyRelevantPoints, that.highlyRelevantPoints)
               && Objects.equals(limitedRelevantPoints, that.limitedRelevantPoints)
               && Objects.equals(littleRelevantPoints, that.littleRelevantPoints)
               && Objects.equals(deletedAt, that.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, highlyRelevantPoints, limitedRelevantPoints, littleRelevantPoints, deletedAt);
    }

    @Override
    public String toString() {
        return "ExperienceType{" + "id=" + id + ", name='" + name + '\'' + ", highlyRelevantPoints="
               + highlyRelevantPoints + ", limitedRelevantPoints=" + limitedRelevantPoints + ", littleRelevantPoints="
               + littleRelevantPoints + '}';
    }
}
