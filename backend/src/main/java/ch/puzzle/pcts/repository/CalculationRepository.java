package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationRepository extends JpaRepository<Calculation, Long> {
}
