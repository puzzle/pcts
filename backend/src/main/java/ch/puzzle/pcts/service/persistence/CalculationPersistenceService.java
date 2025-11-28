package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.repository.CalculationRepository;
import org.springframework.stereotype.Service;

@Service
public class CalculationPersistenceService extends PersistenceBase<Calculation, CalculationRepository> {

    public CalculationPersistenceService(CalculationRepository repository) {
        super(repository);
    }
}
