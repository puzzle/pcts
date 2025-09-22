package ch.puzzle.pcts.model.experienceType;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class ExperienceType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_example")
    private Long id;

    private String name;

    private BigDecimal highlyRelevantPoints;

    private BigDecimal limitedRelevantPoints;

    private BigDecimal littleRelevantPoints;

    public ExperienceType(Long id, String name, BigDecimal highlyRelevantPoints, BigDecimal limitedRelevantPoints,
                          BigDecimal littleRelevantPoints) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "ExperienceType{" + "id=" + id + ", name='" + name + '\'' + ", highlyRelevantPoints="
               + highlyRelevantPoints + ", limitedRelevantPoints=" + limitedRelevantPoints + ", littleRelevantPoints="
               + littleRelevantPoints + '}';
    }

}
