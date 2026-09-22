package ch.puzzle.pcts.model.calculation.degreecalculation;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationChildInterface;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.util.validation.PCTSStringValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    @PCTSStringValidation(nullable = true, allowOnlyWhiteSpaces = true)
    private String comment;

    @ElementCollection
    @MapKeyColumn(name = "relevancy")
    @Column(name = "weight")
    @CollectionTable(name = "degree_calculation_weight", joinColumns = @JoinColumn(name = "degree_calculation_id"))
    private Map<Relevancy, BigDecimal> relevancies = new HashMap<>();

    public DegreeCalculation(Builder builder) {
        this.id = builder.id;
        this.calculation = builder.calculation;
        this.degree = builder.degree;
        this.comment = builder.comment;
        this.relevancies = builder.relevancies;
    }

    public DegreeCalculation() {
    }

    public DegreeCalculation(Long id, Calculation calculation, Degree degree, Map<Relevancy, BigDecimal> relevancies,
                             String comment) {
        this.id = id;
        this.calculation = calculation;
        this.degree = degree;
        this.comment = comment;
        this.relevancies = relevancies;
    }

    @Override
    public String toString() {
        return "DegreeCalculation{" + "id=" + id + ", calculation=" + calculation + ", degree=" + degree
               + ", comment='" + comment + ", relevanies'" + relevancies + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DegreeCalculation that))
            return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCalculation(), that.getCalculation())
               && Objects.equals(getDegree(), that.getDegree())
               && Objects.equals(getComment(), that.getComment())
                && Objects.equals(getRelevancies(), that.getRelevancies());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getCalculation() != null ? getCalculation().getId() : null,
                      getDegree(),
                      getComment(),
                      getRelevancies());
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = trim(comment);
    }

    public Map<Relevancy, BigDecimal> getRelevancies() {
        return relevancies;
    }

    public void setRelevancies(Map<Relevancy, BigDecimal> relevancies) {
        this.relevancies = relevancies;
    }

    public static final class Builder {
        private Long id;
        private Calculation calculation;
        private Degree degree;
        private String comment;
        private Map<Relevancy, BigDecimal> relevancies;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withCalculation(Calculation calculation) {
            this.calculation = calculation;
            return this;
        }

        public Builder withDegree(Degree degree) {
            this.degree = degree;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = trim(comment);
            return this;
        }

        public Builder withRelevancy(Map<Relevancy, BigDecimal> relevancies) {
            this.relevancies = relevancies;
            return this;
        }

        public DegreeCalculation build() {
            return new DegreeCalculation(this);
        }
    }
}
