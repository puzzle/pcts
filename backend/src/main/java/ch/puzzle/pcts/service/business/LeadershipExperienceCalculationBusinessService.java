package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperienceCalculationBusinessService
        extends
            CalculationBusinessService<LeadershipExperienceCalculation> {

    public LeadershipExperienceCalculationBusinessService(CalculationValidationService validationService,
                                                          CalculationPersistenceService persistenceService,
                                                          ExperienceCalculationBusinessService experienceCalculationBusinessService,
                                                          CertificateCalculationBusinessService certificateCalculationBusinessService,
                                                          DegreeCalculationBusinessService degreeCalculationBusinessService) {
        super(validationService,
              persistenceService,
              experienceCalculationBusinessService,
              certificateCalculationBusinessService,
              degreeCalculationBusinessService);
    }

    public BigDecimal getLeadershipExperiencePoints(Long calculationId) {
        List<LeadershipExperienceCalculation> calculations = getByCalculationId(calculationId);

        return calculations
                .stream()
                .filter(this::isEligibleForPoints) // Kept the filter in case you need to enforce rules!
                .map(this::extractPoints)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean isEligibleForPoints(LeadershipExperienceCalculation calculation) {
        // Apply whatever the new rule is for Leadership Experience.
        // For example, perhaps it requires a management role:
        return calculation.getCalculation().getRole().getIsManagement();

        // If ALL leadership experiences automatically grant points,
        // you can just return true, or remove the .filter() step entirely.
    }

    private BigDecimal extractPoints(LeadershipExperienceCalculation calculation) {
        // Assuming your new domain model structure mirrors the old Certificate one.
        // Adjust these getters to match your actual entities!
        return calculation.getLeadershipExperience().getLeadershipExperienceType().getPoints();
    }

    // Note: You will need this method if it's not already in
    // CalculationBusinessService
    // public List<LeadershipExperienceCalculation> getByCalculationId(Long
    // calculationId) { ... }
}