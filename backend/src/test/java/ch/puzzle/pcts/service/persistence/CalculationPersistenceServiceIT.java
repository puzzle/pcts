package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.CalculationRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<Calculation, CalculationRepository, CalculationPersistenceService> {

    private static final Long CALCULATION_ID_1 = 2L;
    private static final Long CALCULATION_ID_2 = 3L;

    @Autowired
    CalculationPersistenceServiceIT(CalculationPersistenceService service) {
        super(service);
    }

    @Override
    Calculation getModel() {
        return Calculation.Builder
                .builder()
                .withMember(MEMBER_1)
                .withRole(ROLE_1)
                .withState(CalculationState.ACTIVE)
                .withPublicationDate(LocalDate.of(2021, 12, 9))
                .withPublicizedBy("Ldap User")
                .withDegreeCalculations(Collections.emptyList())
                .withExperienceCalculations(Collections.emptyList())
                .withCertificateCalculations(Collections.emptyList())
                .build();
    }

    @Override
    List<Calculation> getAll() {
        return CALCULATIONS;
    }

    @Override
    void shouldDelete() {
        // no delete function because we archive instead of deleting
    }

    @DisplayName("Should only have one active Calculation after save when member already has active Calculation for the same role.")
    @Transactional
    @Test
    void shouldOnlyHaveOneActiveCalculationAfterSave() {
        Calculation oldActiveCalculation = getModel();
        Calculation activeCalculation = getModel();
        activeCalculation.setPublicationDate(null);
        activeCalculation.setPublicizedBy(null);

        persistenceService.save(oldActiveCalculation);

        Calculation result = persistenceService.save(activeCalculation);

        assertEquals(LocalDate.now(), result.getPublicationDate());
        assertEquals("Ldap User", result.getPublicizedBy());
        assertThat(getActiveCalculationsOfMember(activeCalculation.getRole(), activeCalculation.getMember()))
                .containsExactly(activeCalculation);
    }

    @DisplayName("Should only have one active Calculation after save when member already has active Calculation for the same role.")
    @Transactional
    @Test
    void shouldOnlyHaveOneActiveCalculationAfterUpdate() {
        Calculation oldActiveCalculation = getModel();
        Calculation activeCalculation = getModel();
        activeCalculation.setPublicationDate(null);
        activeCalculation.setPublicizedBy(null);

        oldActiveCalculation.setId(CALCULATION_ID_1);
        persistenceService.save(oldActiveCalculation);

        activeCalculation.setId(CALCULATION_ID_2);
        Calculation result = persistenceService.save(activeCalculation);

        assertEquals(LocalDate.now(), result.getPublicationDate());
        assertEquals("Ldap User", result.getPublicizedBy());
        assertThat(getActiveCalculationsOfMember(activeCalculation.getRole(), activeCalculation.getMember()))
                .containsExactly(result);
    }

    @DisplayName("Should get all Calculations for a given Member")
    @Test
    void shouldGetAllByMember() {
        List<Calculation> results = persistenceService.getAllByMember(MEMBER_1);
        assertThat(results).isNotEmpty().allMatch(c -> c.getMember().equals(MEMBER_1));
    }

    @DisplayName("Should get all Calculations for a given Member and Role")
    @Test
    void shouldGetAllByMemberAndRole() {
        List<Calculation> results = persistenceService.getAllByMemberAndRole(MEMBER_2, ROLE_2);
        assertThat(results).isNotEmpty().allMatch(c -> c.getMember().equals(MEMBER_2) && c.getRole().equals(ROLE_2));
    }

    private List<Calculation> getActiveCalculationsOfMember(Role role, Member member) {
        return persistenceService
                .getAll()
                .stream()
                .filter(c -> c.getState() == CalculationState.ACTIVE && c.getRole().equals(role)
                             && c.getMember().equals(member))
                .toList();
    }
}
