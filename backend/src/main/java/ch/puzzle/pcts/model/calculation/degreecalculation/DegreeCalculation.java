package ch.puzzle.pcts.model.calculation.degreecalculation;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.degree.Degree;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class DegreeCalculation implements Model {
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
    private BigDecimal weight;

    public DegreeCalculation(Long id, Calculation calculation, Degree degree, Relevancy relevancy, BigDecimal weight) {
        this.id = id;
        this.calculation = calculation;
        this.degree = degree;
        this.relevancy = relevancy;
        this.weight = weight;
    }

    public DegreeCalculation() {

    }

    @Override
    public String toString() {
        return "DegreeCalculation{" + "id=" + id + ", calculation=" + calculation + ", degree=" + degree
               + ", relevancy=" + relevancy + ", weight=" + weight + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        DegreeCalculation that = (DegreeCalculation) object;
        return Objects.equals(id, that.id) && Objects.equals(calculation, that.calculation)
               && Objects.equals(degree, that.degree) && relevancy == that.relevancy
               && Objects.equals(weight, that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, calculation, degree, relevancy, weight);
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
}
