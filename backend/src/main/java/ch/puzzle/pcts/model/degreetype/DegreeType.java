package ch.puzzle.pcts.model.degreetype;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.validation.basic_string_validation.BasicStringValidation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLDelete(sql = "UPDATE degree_type SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class DegreeType implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @BasicStringValidation
    private String name;

    @NotNull(message = "{attribute.not.null}")
    @PositiveOrZero(message = "{attribute.not.negative}")
    private BigDecimal highlyRelevantPoints;

    @NotNull(message = "{attribute.not.null}")
    @PositiveOrZero(message = "{attribute.not.negative}")
    private BigDecimal limitedRelevantPoints;

    @NotNull(message = "{attribute.not.null}")
    @PositiveOrZero(message = "{attribute.not.negative}")
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
    public String toString() {
        return "DegreeType{" + "id=" + id + ", name='" + name + '\'' + ", highlyRelevantPoints=" + highlyRelevantPoints
               + ", limitedRelevantPoints=" + limitedRelevantPoints + ", littleRelevantPoints=" + littleRelevantPoints
               + '}';
    }
}
