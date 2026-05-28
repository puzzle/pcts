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
    private final ExperienceCalculationRepository experienceCalculationRepository;

    public ExperienceCalculationPersistenceService(ExperienceCalculationRepository experienceCalculationRepository) {
        super(experienceCalculationRepository);
        this.experienceCalculationRepository = experienceCalculationRepository;
    }

    public List<ExperienceCalculation> getByCalculationId(Long calculationId) {
        return experienceCalculationRepository.findByCalculationId(calculationId);
    }

    public List<ExperienceCalculation> getByExperienceId(Long experienceId) {
        return experienceCalculationRepository.findByExperienceId(experienceId);
    }

    public void deleteAllById(List<Long> ids) {
        experienceCalculationRepository.deleteAllById(ids);
    }

    @Override
    public String entityName() {
        return EXPERIENCE_CALCULATION;
    }
}
