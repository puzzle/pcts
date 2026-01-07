package ch.puzzle.pcts.model.calculation.experiencecalculation;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationChild;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.experience.Experience;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class ExperienceCalculation implements CalculationChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id")
    private Calculation calculation;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experience_id")
    private Experience experience;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "{attribute.not.null}")
    private Relevancy relevancy;

    private String comment;

    public ExperienceCalculation(Long id, Calculation calculation, Experience experience, Relevancy relevancy,
                                 String comment) {
        this.id = id;
        this.calculation = calculation;
        this.experience = experience;
        this.relevancy = relevancy;
        this.comment = trim(comment);
    }

    public ExperienceCalculation() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public Relevancy getRelevancy() {
        return relevancy;
    }

    public void setRelevancy(Relevancy relevancy) {
        this.relevancy = relevancy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = trim(comment);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ExperienceCalculation that))
            return false;
        return Objects.equals(getId(), that.getId())
               && Objects
                       .equals(this.getCalculation() != null ? this.getCalculation().getId() : null,
                               that.getCalculation() != null ? that.getCalculation().getId() : null)
               && Objects.equals(getExperience(), that.getExperience()) && getRelevancy() == that.getRelevancy()
               && Objects.equals(getComment(), that.getComment());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getCalculation() != null ? getCalculation().getId() : null,
                      getExperience(),
                      getRelevancy(),
                      getComment());
    }

    @Override
    public String toString() {
        return "ExperienceCalculation{" + "id=" + id + ", calculationId="
               + (calculation != null ? calculation.getId() : null) + ", experience=" + experience + ", relevancy="
               + relevancy + ", comment='" + comment + '\'' + '}';
    }
}
