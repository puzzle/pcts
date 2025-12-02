package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.repository.CalculationRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculationPersistenceService extends PersistenceBase<Calculation, CalculationRepository> {

    private final CalculationRepository repository;

    public CalculationPersistenceService(CalculationRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Calculation save(Calculation calculation) {
        if (calculation.getState() == CalculationState.ACTIVE) {
            setPublicationFields(calculation);
            setStateOfOldActiveCalculationsToArchived(calculation);
        }
        return super.save(calculation);
    }

    private void setPublicationFields(Calculation calculation) {
        calculation.setPublicationDate(LocalDate.now());
        // TODO: Replace this with the Ldap's username executing the request
        calculation.setPublicizedBy("Ldap User");
    }

    private void setStateOfOldActiveCalculationsToArchived(Calculation calculation) {
        List<Calculation> activeCalculations = repository
                .getAllByMemberIdAndRoleIdAndState(calculation.getMember().getId(),
                                                   calculation.getRole().getId(),
                                                   CalculationState.ACTIVE);
        activeCalculations.forEach(activeCalculation -> {
            if (!activeCalculation.getId().equals(calculation.getId())) {
                activeCalculation.setState(CalculationState.ARCHIVED);
                repository.save(activeCalculation);
            }
        });
    }
}
