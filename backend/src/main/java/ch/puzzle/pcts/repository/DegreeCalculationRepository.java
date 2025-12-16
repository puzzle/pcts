package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DegreeCalculationRepository extends JpaRepository<DegreeCalculation, Long> {
    List<DegreeCalculation> findByCalculationId(Long calculationId);
    List<DegreeCalculation> findByDegreeId(Long degreeId);
}
