package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.repository.CalculationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculationPersistenceService extends PersistenceBase<Calculation, CalculationRepository> {

    private final CalculationRepository repository;

    public CalculationPersistenceService(CalculationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Calculation> getAllByMemberIdAndRoleIdAndState(Long memberId, Long roleId, CalculationState state) {
        return repository.getAllByMemberIdAndRoleIdAndState(memberId, roleId, state);
    }
}
