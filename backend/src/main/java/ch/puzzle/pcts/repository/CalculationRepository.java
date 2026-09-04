package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculationRepository extends JpaRepository<Calculation, Long> {
    List<Calculation> getAllByMemberIdAndRoleIdAndState(Long memberId, Long roleId, CalculationState state);

    List<Calculation> findAllByMember(Member member);

    List<Calculation> findAllByMemberAndState(Member member, CalculationState state);

    List<Calculation> findAllByMemberAndRole(Member member, Role role);
}
