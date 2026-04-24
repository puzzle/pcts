package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.service.persistence.LeadershipExperienceCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.LeadershipExperienceCalculationValidationService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceCalculationBusinessService extends BusinessBase<LeadershipExperienceCalculation> {
    private final LeadershipExperienceCalculationValidationService leadershipExperienceCalculationValidationService;
    private final LeadershipExperienceCalculationPersistenceService leadershipExperienceCalculationPersistenceService;

    protected LeadershipExperienceCalculationBusinessService(LeadershipExperienceCalculationValidationService validationService,
                                                             LeadershipExperienceCalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
        this.leadershipExperienceCalculationValidationService = validationService;
        this.leadershipExperienceCalculationPersistenceService = persistenceService;
    }

    public List<LeadershipExperienceCalculation> getByCalculationId(Long calculationId) {
        return leadershipExperienceCalculationPersistenceService.getByCalculationId(calculationId);
    }

    public List<LeadershipExperienceCalculation> getByLeadershipExperienceId(Long leadershipExperienceId) {
        return leadershipExperienceCalculationPersistenceService.getByLeadershipExperienceId(leadershipExperienceId);
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

    @Override
    public LeadershipExperienceCalculation update(Long id,
                                                  LeadershipExperienceCalculation leadershipExperienceCalculation) {
        leadershipExperienceCalculationValidationService.validateOnUpdate(id, leadershipExperienceCalculation);
        List<LeadershipExperienceCalculation> existing = getByLeadershipExperienceId(leadershipExperienceCalculation
                .getLeadershipExperience()
                .getId());
        leadershipExperienceCalculationValidationService
                .validateDuplicateLeadershipExperienceId(leadershipExperienceCalculation, existing);
        return leadershipExperienceCalculationPersistenceService.save(leadershipExperienceCalculation);
    }

    @Override
    public LeadershipExperienceCalculation create(LeadershipExperienceCalculation leadershipExperienceCalculation) {
        leadershipExperienceCalculationValidationService.validateOnCreate(leadershipExperienceCalculation);
        List<LeadershipExperienceCalculation> existing = getByLeadershipExperienceId(leadershipExperienceCalculation
                .getLeadershipExperience()
                .getId());
        leadershipExperienceCalculationValidationService
                .validateDuplicateLeadershipExperienceId(leadershipExperienceCalculation, existing);
        return leadershipExperienceCalculationPersistenceService.save(leadershipExperienceCalculation);
    }

    public List<LeadershipExperienceCalculation> createLeadershipExperienceCalculations(Calculation calculation) {
        return calculation.getLeadershipExperienceCalculations().stream().map(expCalc -> {
            expCalc.setCalculation(calculation);
            return create(expCalc);
        }).toList();
    }

    public List<LeadershipExperienceCalculation> updateLeadershipExperienceCalculations(Calculation calculation) {
        List<LeadershipExperienceCalculation> existing = getByCalculationId(calculation.getId());

        List<LeadershipExperienceCalculation> leadershipExperienceCalculations = calculation
                .getLeadershipExperienceCalculations()
                .stream()
                .map(leadershipExperienceCalculation -> {
                    leadershipExperienceCalculation.setCalculation(calculation);
                    return leadershipExperienceCalculation.getId() == null ? create(leadershipExperienceCalculation)
                            : update(leadershipExperienceCalculation.getId(), leadershipExperienceCalculation);
                })
                .toList();
        deleteUnusedLeadershipExperienceCalculations(existing, leadershipExperienceCalculations);

        return leadershipExperienceCalculations;
    }

    private void deleteUnusedLeadershipExperienceCalculations(List<LeadershipExperienceCalculation> existing,
                                                              List<LeadershipExperienceCalculation> updated) {
        existing.removeAll(updated);

        leadershipExperienceCalculationPersistenceService
                .deleteAllById(existing.stream().map(LeadershipExperienceCalculation::getId).toList());
    }
}