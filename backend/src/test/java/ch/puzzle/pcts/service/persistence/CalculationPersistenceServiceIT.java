package ch.puzzle.pcts.service.persistence;

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
        return Calculation.Builder
                .builder()
                .withMember(memberPersistenceServiceIT.getAll().getFirst())
                .withRole(rolePersistenceServiceIT.getAll().getFirst())
                .withState(CalculationState.ACTIVE)
                .withPublicationDate(LocalDate.of(2021, 12, 9))
                .withPublicizedBy("Ldap User")
                .withDegrees(Collections.emptyList())
                .withExperiences(Collections.emptyList())
                .withCertificates(Collections.emptyList())
                .build();
    }

    @Override
    List<Calculation> getAll() {
        return List
                .of(Calculation.Builder
                        .builder()
                        .withId(1L)
                        .withMember(memberPersistenceServiceIT.getAll().getFirst())
                        .withRole(rolePersistenceServiceIT.getAll().getLast())
                        .withState(CalculationState.DRAFT)
                        .withPublicationDate(LocalDate.of(2025, 1, 14))
                        .withPublicizedBy("Ldap User")
                        .withDegrees(Collections.emptyList())
                        .withExperiences(Collections.emptyList())
                        .withCertificates(Collections.emptyList())
                        .build(),
                    Calculation.Builder
                            .builder()
                            .withId(2L)
                            .withMember(memberPersistenceServiceIT.getAll().getLast())
                            .withRole(rolePersistenceServiceIT.getAll().getLast())
                            .withState(CalculationState.ARCHIVED)
                            .withPublicationDate(LocalDate.of(2025, 1, 14))
                            .withPublicizedBy("Ldap User 2")
                            .withDegrees(Collections.emptyList())
                            .withExperiences(Collections.emptyList())
                            .withCertificates(Collections.emptyList())
                            .build(),
                    Calculation.Builder
                            .builder()
                            .withId(3L)
                            .withMember(memberPersistenceServiceIT.getAll().getLast())
                            .withRole(rolePersistenceServiceIT.getAll().getLast())
                            .withState(CalculationState.ACTIVE)
                            .withDegrees(Collections.emptyList())
                            .withExperiences(Collections.emptyList())
                            .withCertificates(Collections.emptyList())
                            .build());
    }

    @DisplayName("Should only have one active Calculation after save when member already has active Calculation for the same role.")
    @Transactional
    @Test
    void shouldOnlyHaveOneActiveCalculationAfterSave() {
        Calculation oldActiveCalculation = getModel();
        Calculation activeCalculation = getModel();
        activeCalculation.setPublicationDate(null);
        activeCalculation.setPublicizedBy(null);

        service.save(oldActiveCalculation);

        Calculation result = service.save(activeCalculation);

        assertEquals(LocalDate.now(), result.getPublicationDate());
        assertEquals("Ldap User", result.getPublicizedBy());
        assertThat(getActiveCalculations(activeCalculation.getRole(), activeCalculation.getMember()))
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

        oldActiveCalculation.setId(2L);
        service.save(oldActiveCalculation);

        activeCalculation.setId(3L);
        Calculation result = service.save(activeCalculation);

        assertEquals(LocalDate.now(), result.getPublicationDate());
        assertEquals("Ldap User", result.getPublicizedBy());
        List<Calculation> activeCalcs = getActiveCalculations(activeCalculation.getRole(),
                                                              activeCalculation.getMember());
        assertThat(activeCalcs).containsExactly(result);
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
