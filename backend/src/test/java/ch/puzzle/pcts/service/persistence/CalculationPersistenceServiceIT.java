package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.repository.CalculationRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
        Calculation calculation = getModel();
        // Setting Member and Role to a already active calculation
        calculation.setMember(memberPersistenceServiceIT.getAll().getLast());
        calculation.setRole(rolePersistenceServiceIT.getAll().getLast());

        Calculation result = service.save(calculation);

        calculation.setId(result.getId());
        assertThat(result).isEqualTo(calculation);

        assertThat(getActiveCalculations()).containsExactly(calculation);
    }

    @DisplayName("Should only have one active Calculation after update")
    @Transactional
    @Test
    void shouldOnlyHaveOneActiveCalculationAfterUpdate() {
        Long id = 2L;
        Calculation calculation = getModel();
        // Setting Member and Role to a already active calculation
        calculation.setMember(memberPersistenceServiceIT.getAll().getLast());
        calculation.setRole(rolePersistenceServiceIT.getAll().getLast());

        calculation.setId(id);
        service.save(calculation);

        Optional<Calculation> result = service.getById(id);

        assertThat(result).isPresent();
        assertEquals(calculation, result.get());

        assertThat(getActiveCalculations()).containsExactly(calculation);
    }

    private List<Calculation> getActiveCalculations() {
        return service
                .getAll()
                .stream()
                .filter(calculation1 -> calculation1.getState().equals(CalculationState.ACTIVE))
                .toList();
    }
}
