package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE_CALCULATION;

import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.repository.LeadershipExperienceCalculationRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceCalculationPersistenceService
        extends
            PersistenceBase<LeadershipExperienceCalculation, LeadershipExperienceCalculationRepository> {
    private final LeadershipExperienceCalculationRepository repository;

    protected LeadershipExperienceCalculationPersistenceService(LeadershipExperienceCalculationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<LeadershipExperienceCalculation> getByCalculationId(Long calculationId) {
        return repository.findByCalculationId(calculationId);
    }

    public List<LeadershipExperienceCalculation> getByLeadershipExperienceId(Long leadershipExperienceId) {
        return repository.findByLeadershipExperienceId(leadershipExperienceId);
    }

    public void deleteAllByIdInBatch(List<Long> ids) {
        repository.deleteAllByIdInBatch(ids);
    }

    @Override
    public String entityName() {
        return LEADERSHIP_EXPERIENCE_CALCULATION;
    }
}
