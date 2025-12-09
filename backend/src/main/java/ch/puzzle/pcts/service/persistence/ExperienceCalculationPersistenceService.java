package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
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

    public List<ExperienceCalculation> getByCalculationId(Long calculationId) {
        return this.repository.findByCalculationId(calculationId);
    }

    public List<ExperienceCalculation> getByExperience(Experience experience) {
        return this.repository.findByExperience(experience);
    }

}
