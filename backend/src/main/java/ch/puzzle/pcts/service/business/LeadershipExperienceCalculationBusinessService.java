package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.service.persistence.LeadershipExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceCalculationValidationService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceCalculationBusinessService extends BusinessBase<LeadershipExperienceCalculation> {
    private final LeadershipExperienceCalculationPersistenceService leadershipExperienceCalculationPersistenceService;

    public LeadershipExperienceCalculationBusinessService(LeadershipExperienceCalculationValidationService validationService,
                                                          LeadershipExperienceCalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
        this.leadershipExperienceCalculationPersistenceService = persistenceService;
    }

    public List<LeadershipExperienceCalculation> getByCalculationId(Long calculationId) {
        return leadershipExperienceCalculationPersistenceService.getByCalculationId(calculationId);
    }

    public BigDecimal getLeadershipExperiencePoints(Long calculationId) {
        List<LeadershipExperienceCalculation> calculations = getByCalculationId(calculationId);

        return calculations
                .stream()
                .filter(this::isEligibleForPoints)
                .map(this::extractPoints)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isEligibleForPoints(LeadershipExperienceCalculation calculation) {
        return calculation.getCalculation().getRole().getIsManagement();
    }

    private BigDecimal extractPoints(LeadershipExperienceCalculation calculation) {
        return calculation.getLeadershipExperience().getLeadershipExperienceType().getPoints();
    }
}