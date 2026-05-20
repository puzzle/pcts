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
    private final DegreeCalculationRepository degreeCalculationRepository;

    public DegreeCalculationPersistenceService(DegreeCalculationRepository degreeCalculationRepository) {
        super(degreeCalculationRepository);
        this.degreeCalculationRepository = degreeCalculationRepository;
    }

    public List<DegreeCalculation> getByCalculationId(Long calculationId) {
        return degreeCalculationRepository.findByCalculationId(calculationId);
    }

    public List<DegreeCalculation> getByDegreeId(Long degreeId) {
        return degreeCalculationRepository.findByDegreeId(degreeId);
    }

    public void deleteAllById(List<Long> ids) {
        degreeCalculationRepository.deleteAllById(ids);
    }

    @Override
    public String entityName() {
        return DEGREE_CALCULATION;
    }
}
