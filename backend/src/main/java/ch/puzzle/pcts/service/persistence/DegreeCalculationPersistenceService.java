package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.DEGREE_CALCULATION;

import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.repository.DegreeCalculationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DegreeCalculationPersistenceService
        extends
            PersistenceBase<DegreeCalculation, DegreeCalculationRepository> {
    private final DegreeCalculationRepository repository;

    public DegreeCalculationPersistenceService(DegreeCalculationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<DegreeCalculation> getByCalculationId(Long calculationId) {
        return this.repository.findByCalculationId(calculationId);
    }

    public List<DegreeCalculation> getByDegreeId(Long degreeId) {
        return this.repository.findByDegreeId(degreeId);
    }

    @Override
    public String entityName() {
        return DEGREE_CALCULATION;
    }
}
