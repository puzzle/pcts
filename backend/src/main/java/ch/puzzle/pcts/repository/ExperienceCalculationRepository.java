package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceCalculationRepository extends CalculationRelationRepository<ExperienceCalculation, Long> {
    List<ExperienceCalculation> findByExperienceId(Long experienceId);
}
