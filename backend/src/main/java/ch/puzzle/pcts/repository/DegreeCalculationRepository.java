package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeCalculationRepository extends CalculationChildRepository<DegreeCalculation, Long> {
    List<DegreeCalculation> findByDegreeId(Long degreeId);
}
