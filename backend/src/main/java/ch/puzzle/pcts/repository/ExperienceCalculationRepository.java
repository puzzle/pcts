package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExperienceCalculationRepository extends JpaRepository<ExperienceCalculation, Long> {
    List<ExperienceCalculation> findByCalculationId(Long calculationId);
    List<ExperienceCalculation> findByExperienceId(Long experienceId);
}
