package ch.puzzle.pcts.model.calculation.experiencecalculation;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.experience.Experience;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class ExperienceCalculation implements Model {
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

    public ExperienceCalculation(Long id, Calculation calculation, Experience experience, Relevancy relevancy) {
        this.id = id;
        this.calculation = calculation;
        this.experience = experience;
        this.relevancy = relevancy;
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

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        ExperienceCalculation that = (ExperienceCalculation) object;
        return Objects.equals(getId(), that.getId())
               && Objects
                       .equals(this.getCalculation() != null ? this.getCalculation().getId() : null,
                               that.getCalculation() != null ? that.getCalculation().getId() : null)
               && Objects.equals(getExperience(), that.getExperience()) && getRelevancy() == that.getRelevancy();
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(),
                      getCalculation() != null ? getCalculation().getId() : null,
                      getExperience(),
                      getRelevancy());
    }

    @Override
    public String toString() {
        return "ExperienceCalculation{" + "id=" + id + ", calculationId="
               + (calculation != null ? calculation.getId() : null) + ", experience=" + experience + ", relevancy="
               + relevancy + '}';
    }
}
