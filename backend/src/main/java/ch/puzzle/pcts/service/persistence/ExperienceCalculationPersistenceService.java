package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.repository.ExperienceCalculationRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperienceCalculationPersistenceService
        extends
            PersistenceBase<ExperienceCalculation, ExperienceCalculationRepository> {
    public ExperienceCalculationPersistenceService(ExperienceCalculationRepository repository) {
        super(repository);
    }
}
