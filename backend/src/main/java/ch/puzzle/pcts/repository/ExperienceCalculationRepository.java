package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceCalculationRepository extends JpaRepository<ExperienceCalculation, Long> {
}
