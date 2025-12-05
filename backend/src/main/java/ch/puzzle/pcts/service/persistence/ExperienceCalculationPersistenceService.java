package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.repository.ExperienceCalculationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExperienceCalculationPersistenceService
        extends
            PersistenceBase<ExperienceCalculation, ExperienceCalculationRepository> {
    private final ExperienceCalculationRepository repository;

    public ExperienceCalculationPersistenceService(ExperienceCalculationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<ExperienceCalculation> getByCalculation(Calculation calculation) {
        return this.repository.findByCalculation(calculation);
    }

}
