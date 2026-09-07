package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.CalculationRepository;
import ch.puzzle.pcts.service.JwtService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculationPersistenceService extends PersistenceBase<Calculation, CalculationRepository> {
    private final JwtService jwtService;
    private final CalculationRepository calculationRepository;

    public CalculationPersistenceService(JwtService jwtService, CalculationRepository calculationRepository) {
        super(calculationRepository);
        this.jwtService = jwtService;
        this.calculationRepository = calculationRepository;
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
        calculation.setPublicizedBy(this.jwtService.getDisplayName());
    }

    private void setStateOfOldActiveCalculationsToArchived(Calculation calculation) {
        List<Calculation> activeCalculations = calculationRepository
                .getAllByMemberIdAndRoleIdAndState(calculation.getMember().getId(),
                                                   calculation.getRole().getId(),
                                                   CalculationState.ACTIVE);
        activeCalculations.forEach(activeCalculation -> {
            if (!activeCalculation.getId().equals(calculation.getId())) {
                activeCalculation.setState(CalculationState.ARCHIVED);
                calculationRepository.save(activeCalculation);
            }
        });
    }

    public List<Calculation> getAllByMember(Member member) {
        return calculationRepository.findAllByMember(member);
    }

    public List<Calculation> getAllByMemberAndState(Member member, CalculationState state) {
        return calculationRepository.findAllByMemberAndState(member, state);
    }

    public List<Calculation> getAllByMemberAndRole(Member member, Role role) {
        return calculationRepository.findAllByMemberAndRole(member, role);
    }
}
