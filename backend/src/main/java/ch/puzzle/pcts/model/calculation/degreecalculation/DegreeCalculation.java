package ch.puzzle.pcts.model.calculation.degreecalculation;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationChild;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.degree.Degree;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import org.hibernate.validator.constraints.Range;

@Entity
public class DegreeCalculation implements CalculationChild, Model {
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

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{attribute.not.null}")
    private Relevancy relevancy;

    @NotNull(message = "{attribute.not.null}")
    @Range(min = 1, max = 100, message = "{attribute.size.between}")
    private BigDecimal weight;

    private String comment;

    public DegreeCalculation(Long id, Calculation calculation, Degree degree, Relevancy relevancy, BigDecimal weight,
                             String comment) {
        this.id = id;
        this.calculation = calculation;
        this.degree = degree;
        this.relevancy = relevancy;
        this.weight = weight;
        this.comment = trim(comment);
    }

    public DegreeCalculation() {

    }

    @Override
    public String toString() {
        return "DegreeCalculation{" + "id=" + id + ", calculationId="
               + (calculation != null ? calculation.getId() : null) + ", degree=" + degree + ", relevancy=" + relevancy
               + ", weight=" + weight + ", comment='" + comment + '\'' + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DegreeCalculation that))
            return false;
        return Objects.equals(getId(), that.getId())
               && Objects
                       .equals(this.getCalculation() != null ? this.getCalculation().getId() : null,
                               that.getCalculation() != null ? that.getCalculation().getId() : null)
               && Objects.equals(getDegree(), that.getDegree()) && getRelevancy() == that.getRelevancy()
               && Objects.equals(getWeight(), that.getWeight()) && Objects.equals(getComment(), that.getComment());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getCalculation() != null ? getCalculation().getId() : null,
                      getDegree(),
                      getRelevancy(),
                      getWeight(),
                      getComment());
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

    public Relevancy getRelevancy() {
        return relevancy;
    }

    public void setRelevancy(Relevancy relevancy) {
        this.relevancy = relevancy;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = trim(comment);
    }
}
