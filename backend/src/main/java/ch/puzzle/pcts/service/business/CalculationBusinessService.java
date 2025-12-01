package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.repository.CalculationRepository;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculationBusinessService
        extends
            BusinessBase<Calculation, CalculationValidationService, CalculationRepository, CalculationPersistenceService> {

    protected CalculationBusinessService(CalculationValidationService validationService,
                                         CalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }

    @Override
    public Calculation create(Calculation calculation) {
        setPublicationFieldsAndChangeOldActiveCalculationToArchived(null, calculation);
        return super.create(calculation);
    }

    @Override
    public Calculation update(Long id, Calculation calculation) {
        setPublicationFieldsAndChangeOldActiveCalculationToArchived(id, calculation);
        return super.update(id, calculation);
    }

    protected void setPublicationFieldsAndChangeOldActiveCalculationToArchived(Long id, Calculation calculation) {
        if (calculation.getState() == CalculationState.ACTIVE) {
            calculation.setPublicationDate(LocalDate.now());
            // TODO: Replace this with the Ldap's username executing the request
            calculation.setPublicizedBy("Ldap User");

            List<Calculation> activeCalculations = persistenceService
                    .getAllByMemberIdAndRoleIdAndState(calculation.getMember().getId(),
                                                       calculation.getRole().getId(),
                                                       CalculationState.ACTIVE);
            if (!activeCalculations.isEmpty() && (id == null || !id.equals(activeCalculations.getFirst().getId()))) {
                Calculation activeCalculation = activeCalculations.getFirst();
                activeCalculation.setState(CalculationState.ARCHIVED);
                this.persistenceService.save(calculation);
            }
        }
    }
}
