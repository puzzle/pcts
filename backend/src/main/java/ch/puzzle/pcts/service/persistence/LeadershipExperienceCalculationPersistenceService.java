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
    private final LeadershipExperienceCalculationRepository leadershipExperienceCalculationRepository;

    protected LeadershipExperienceCalculationPersistenceService(LeadershipExperienceCalculationRepository leadershipExperienceCalculationRepository) {
        super(leadershipExperienceCalculationRepository);
        this.leadershipExperienceCalculationRepository = leadershipExperienceCalculationRepository;
    }

    public List<LeadershipExperienceCalculation> getByCalculationId(Long calculationId) {
        return leadershipExperienceCalculationRepository.findByCalculationId(calculationId);
    }

    public List<LeadershipExperienceCalculation> getByLeadershipExperienceId(Long leadershipExperienceId) {
        return leadershipExperienceCalculationRepository.findByLeadershipExperienceId(leadershipExperienceId);
    }

    public void deleteAllById(List<Long> ids) {
        leadershipExperienceCalculationRepository.deleteAllById(ids);
    }

    @Override
    public String entityName() {
        return LEADERSHIP_EXPERIENCE_CALCULATION;
    }
}
