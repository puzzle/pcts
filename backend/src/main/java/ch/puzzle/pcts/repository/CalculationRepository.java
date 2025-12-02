package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {
    List<Calculation> getAllByMemberIdAndRoleIdAndState(Long memberId, Long roleId, CalculationState state);
}
