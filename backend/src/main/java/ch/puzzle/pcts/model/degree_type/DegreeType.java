package ch.puzzle.pcts.model.degree_type;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE degree_type SET deleted_at = CURRENT_TIMESTAMP WHERE degree_type_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class DegreeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long degreeTypeId;

    private String name;

    private BigDecimal highlyRelevantPoints;

    private BigDecimal limitedRelevantPoints;

    private BigDecimal littleRelevantPoints;

    public DegreeType(Long id, String name, BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
                      BigDecimal littleRelevantPoints) {
        this.degreeTypeId = id;
        this.name = name;
        this.highlyRelevantPoints = highlyRelevantPoints;
        this.limitedRelevantPoints = limitedRelevantPoints;
        this.littleRelevantPoints = littleRelevantPoints;
    }

    public DegreeType() {
    }

    public Long getDegreeTypeId() {
        return degreeTypeId;
    }

    public void setDegreeTypeId(Long degreeTypeId) {
        this.degreeTypeId = degreeTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
