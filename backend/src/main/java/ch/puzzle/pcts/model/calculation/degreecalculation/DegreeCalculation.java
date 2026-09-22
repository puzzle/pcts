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
    @Range(min = 0, max = 100, message = "{attribute.size.between}")
    private BigDecimal strongWeight;

    @NotNull(message = "{attribute.not.null}")
    @Range(min = 0, max = 100, message = "{attribute.size.between}")
    private BigDecimal partlyWeight;

    @NotNull(message = "{attribute.not.null}")
    @Range(min = 0, max = 100, message = "{attribute.size.between}")
    private BigDecimal lessWeight;

    @PCTSStringValidation(nullable = true, allowOnlyWhiteSpaces = true)
    private String comment;

    public DegreeCalculation(Builder builder) {
        this.id = builder.id;
        this.calculation = builder.calculation;
        this.degree = builder.degree;
        this.strongWeight = builder.strongWeight;
        this.partlyWeight = builder.partlyWeight;
        this.lessWeight = builder.lessWeight;
        this.comment = builder.comment;
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
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCalculation(), that.getCalculation())
               && Objects.equals(getDegree(), that.getDegree())
               && Objects.equals(getStrongWeight(), that.getStrongWeight())
               && Objects.equals(getPartlyWeight(), that.getPartlyWeight())
               && Objects.equals(getLessWeight(), that.getLessWeight())
               && Objects.equals(getComment(), that.getComment());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getCalculation(),
                      getDegree(),
                      getStrongWeight(),
                      getPartlyWeight(),
                      getLessWeight(),
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

    public static final class Builder {
        private Long id;
        private Calculation calculation;
        private Degree degree;
        private BigDecimal strongWeight;
        private BigDecimal partlyWeight;
        private BigDecimal lessWeight;
        private String comment;

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

        public Builder withStrongWeight(BigDecimal strongWeight) {
            this.strongWeight = strongWeight;
            return this;
        }

        public Builder withPartlyWeight(BigDecimal partlyWeight) {
            this.partlyWeight = partlyWeight;
            return this;
        }

        public Builder withLessWeight(BigDecimal lessWeight) {
            this.lessWeight = lessWeight;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = trim(comment);
            return this;
        }

        public DegreeCalculation build() {
            return new DegreeCalculation(this);
        }
    }
}
