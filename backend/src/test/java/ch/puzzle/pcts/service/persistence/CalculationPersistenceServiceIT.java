package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.CalculationRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<Calculation, CalculationRepository, CalculationPersistenceService> {

    MemberPersistenceServiceIT memberPersistenceServiceIT;
    RolePersistenceServiceIT rolePersistenceServiceIT;

    @Autowired
    CalculationPersistenceServiceIT(CalculationPersistenceService service,
                                    MemberPersistenceService memberPersistenceService,
                                    RolePersistenceService rolePersistenceService) {
        super(service);
        memberPersistenceServiceIT = new MemberPersistenceServiceIT(memberPersistenceService);
        rolePersistenceServiceIT = new RolePersistenceServiceIT(rolePersistenceService);
    }

    @Override
    Calculation getModel() {
        return new Calculation(null,
                               memberPersistenceServiceIT.getAll().getFirst(),
                               rolePersistenceServiceIT.getAll().getFirst(),
                               CalculationState.ACTIVE,
                               LocalDate.of(2021, 12, 9),
                               "Ldap User");
    }

    @Override
    List<Calculation> getAll() {
        return List
                .of(new Calculation(1L,
                                    memberPersistenceServiceIT.getAll().getFirst(),
                                    rolePersistenceServiceIT.getAll().getLast(),
                                    CalculationState.DRAFT,
                                    LocalDate.of(2025, 1, 14),
                                    "Ldap User"),
                    new Calculation(2L,
                                    memberPersistenceServiceIT.getAll().getLast(),
                                    rolePersistenceServiceIT.getAll().getLast(),
                                    CalculationState.ARCHIVED,
                                    null,
                                    "Ldap User 2"),
                    new Calculation(3L,
                                    memberPersistenceServiceIT.getAll().getLast(),
                                    rolePersistenceServiceIT.getAll().getLast(),
                                    CalculationState.ACTIVE,
                                    LocalDate.of(2025, 1, 14),
                                    null));
    }

    @DisplayName("Should only have one active Calculation after save when member already has active Calculation for the same role.")
    @Transactional
    @Test
    void shouldOnlyHaveOneActiveCalculationAfterSave() {
        Calculation oldActiveCalculation = getModel();
        Calculation activeCalculation = getModel();

        service.save(oldActiveCalculation);
        service.save(activeCalculation);

        assertThat(getActiveCalculations(activeCalculation.getRole(), activeCalculation.getMember()))
                .containsExactly(activeCalculation);
    }

    @DisplayName("Should only have one active Calculation after save when member already has active Calculation for the same role.")
    @Transactional
    @Test
    void shouldOnlyHaveOneActiveCalculationAfterUpdate() {
        Calculation oldActiveCalculation = getModel();
        Calculation activeCalculation = getModel();

        oldActiveCalculation.setId(2L);
        service.save(oldActiveCalculation);
        activeCalculation.setId(3L);
        service.save(activeCalculation);

        assertThat(getActiveCalculations(activeCalculation.getRole(), activeCalculation.getMember()))
                .containsExactly(activeCalculation);
    }

    private List<Calculation> getActiveCalculations(Role role, Member member) {
        return service
                .getAll()
                .stream()
                .filter(calculation -> calculation.getState().equals(CalculationState.ACTIVE)
                                       && calculation.getRole().equals(role) && calculation.getMember().equals(member))
                .toList();
    }
}
