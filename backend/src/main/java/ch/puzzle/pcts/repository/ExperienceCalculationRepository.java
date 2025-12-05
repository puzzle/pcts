package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceCalculationRepository extends JpaRepository<ExperienceCalculation, Long> {
    List<ExperienceCalculation> findByCalculation(Calculation calculation);
}
