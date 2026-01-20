package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.EXPERIENCE_CALCULATION;

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

    public List<ExperienceCalculation> getByCalculationId(Long calculationId) {
        return repository.findByCalculationId(calculationId);
    }

    public List<ExperienceCalculation> getByExperienceId(Long experienceId) {
        return repository.findByExperienceId(experienceId);
    }

    public void deleteAllByIdInBatch(List<Long> ids) {
        repository.deleteAllByIdInBatch(ids);
    }
    @Override
    public String entityName() {
        return EXPERIENCE_CALCULATION;
    }
}
