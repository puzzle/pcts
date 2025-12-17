package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.CalculationRepository;
import ch.puzzle.pcts.util.AuthenticatedUserHelper;
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

    @Override
    public String entityName() {
        return CALCULATION;
    }

    private void setPublicationFields(Calculation calculation) {
        calculation.setPublicationDate(LocalDate.now());
        calculation.setPublicizedBy(AuthenticatedUserHelper.getDisplayName());
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

    public List<Calculation> getAllByMember(Member member) {
        return repository.findAllByMember(member);
    }

    public List<Calculation> getAllByMemberAndState(Member member, CalculationState state) {
        return repository.findAllByMemberAndState(member, state);
    }

    public List<Calculation> getAllByMemberAndRole(Member member, Role role) {
        return repository.findAllByMemberAndRole(member, role);
    }
}
