package ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationChildInterface;
import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class LeadershipExperienceCalculation implements CalculationChildInterface, Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id")
    private Calculation calculation;

    @NotNull(message = "{attribute.not.null}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leadershipExperience_id")
    private LeadershipExperience leadershipExperience;

    public LeadershipExperienceCalculation(Long id, Calculation calculation,
                                           LeadershipExperience leadershipExperience) {
        this.id = id;
        this.calculation = calculation;
        this.leadershipExperience = leadershipExperience;
    }

    public LeadershipExperienceCalculation(Builder builder) {
        this.id = builder.id;
        this.calculation = builder.calculation;
        this.leadershipExperience = builder.leadershipExperience;
    }

    public LeadershipExperienceCalculation() {
    }

    @Override
    public String toString() {
        return "LeadershipExperienceCalculation{" + "id=" + id + ", calculation=" + getCalculation()
               + ", leadershipExperience=" + leadershipExperience + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LeadershipExperienceCalculation that)) {
            return false;
        }
        return Objects.equals(id, that.id) && Objects.equals(getCalculation(), that.getCalculation())
               && Objects.equals(leadershipExperience, that.leadershipExperience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getCalculation(), leadershipExperience);
    }

    public LeadershipExperience getLeadershipExperience() {
        return leadershipExperience;
    }

    public void setLeadershipExperience(LeadershipExperience leadershipExperience) {
        this.leadershipExperience = leadershipExperience;
    }

    @Override
    public Calculation getCalculation() {
        return calculation;
    }

    @Override
    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static final class Builder {
        private Long id;
        private Calculation calculation;
        private LeadershipExperience leadershipExperience;

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

        public Builder withLeadershipExperience(LeadershipExperience leadershipExperience) {
            this.leadershipExperience = leadershipExperience;
            return this;
        }

        public LeadershipExperienceCalculation build() {
            return new LeadershipExperienceCalculation(this);
        }
    }
}
