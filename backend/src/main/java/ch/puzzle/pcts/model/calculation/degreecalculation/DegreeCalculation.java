package ch.puzzle.pcts.model.calculation.degreecalculation;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationChildInterface;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.util.validation.PCTSStringValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.validator.constraints.Range;

@Entity
public class DegreeCalculation implements CalculationChildInterface, Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id")
    private Calculation calculation;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_id")
    private Degree degree;

    @NotNull(message = "{attribute.not.null}")
    @Range(min = 1, max = 100, message = "{attribute.size.between}")
    private BigDecimal strongWeight;

    @NotNull(message = "{attribute.not.null}")
    @Range(min = 1, max = 100, message = "{attribute.size.between}")
    private BigDecimal partlyWeight;

    @NotNull(message = "{attribute.not.null}")
    @Range(min = 1, max = 100, message = "{attribute.size.between}")
    private BigDecimal lessWeight;

    @PCTSStringValidation(nullable = true, allowOnlyWhiteSpaces = true)
    private String comment;

    public DegreeCalculation(Long id, Calculation calculation, Degree degree, BigDecimal strongWeight,
                             BigDecimal partlyWeight, BigDecimal lessWeight, String comment) {
        this.id = id;
        this.calculation = calculation;
        this.degree = degree;
        this.strongWeight = strongWeight;
        this.partlyWeight = partlyWeight;
        this.lessWeight = lessWeight;
        this.comment = trim(comment);
    }

    public DegreeCalculation() {

    }

    @Override
    public String toString() {
        return "DegreeCalculation{" + "id=" + id + ", calculation=" + calculation + ", degree=" + degree
               + ", strongWeight=" + strongWeight + ", partlyWeight=" + partlyWeight + ", lessWeight=" + lessWeight
               + ", comment='" + comment + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DegreeCalculation that))
            return false;
        return Objects.equals(id, that.id) && Objects.equals(calculation, that.calculation)
               && Objects.equals(degree, that.degree) && Objects.equals(strongWeight, that.strongWeight)
               && Objects.equals(partlyWeight, that.partlyWeight) && Objects.equals(lessWeight, that.lessWeight)
               && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, calculation, degree, strongWeight, partlyWeight, lessWeight, comment);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public BigDecimal getStrongWeight() {
        return strongWeight;
    }

    public void setStrongWeight(BigDecimal strongWeight) {
        this.strongWeight = strongWeight;
    }

    public BigDecimal getPartlyWeight() {
        return partlyWeight;
    }

    public void setPartlyWeight(BigDecimal partlyWeight) {
        this.partlyWeight = partlyWeight;
    }

    public BigDecimal getLessWeight() {
        return lessWeight;
    }

    public void setLessWeight(BigDecimal lessWeight) {
        this.lessWeight = lessWeight;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = trim(comment);
    }
}
