package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.CalculationRepository;
import ch.puzzle.pcts.repository.CertificateTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationPersistenceService extends PersistenceBase<Calculation, CalculationRepository> {

    private final CalculationRepository repository;

    public CalculationPersistenceService(CalculationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Calculation> getAllByMemberIdAndRoleIdAndState(Long memberId, Long roleId, CalculationState state){
        return repository.getAllByMemberIdAndRoleIdAndState(memberId, roleId, state);
    }
}
