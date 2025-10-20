package ch.puzzle.pcts.model.degreetype;

import static org.apache.commons.lang3.StringUtils.trim;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE degree_type SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class DegreeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal highlyRelevantPoints;

    private BigDecimal limitedRelevantPoints;

    private BigDecimal littleRelevantPoints;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DegreeType degreeType)) {
            return false;
        }
        return Objects.equals(id, degreeType.id) && Objects.equals(name, degreeType.name)
               && Objects.equals(highlyRelevantPoints, degreeType.highlyRelevantPoints)
               && Objects.equals(limitedRelevantPoints, degreeType.limitedRelevantPoints)
               && Objects.equals(littleRelevantPoints, degreeType.littleRelevantPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, highlyRelevantPoints, limitedRelevantPoints, littleRelevantPoints);
    }

    @Override
    public String toString() {
        return "DegreeType{" + "id=" + id + ", name='" + name + '\'' + ", highlyRelevantPoints=" + highlyRelevantPoints
               + ", limitedRelevantPoints=" + limitedRelevantPoints + ", littleRelevantPoints=" + littleRelevantPoints
               + '}';
    }
}
