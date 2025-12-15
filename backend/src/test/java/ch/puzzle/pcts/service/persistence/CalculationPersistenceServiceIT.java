package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.repository.CalculationRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
        List<Member> members = memberPersistenceServiceIT.getAll();
        List<Role> roles = rolePersistenceServiceIT.getAll();

        ExperienceType experienceType1 = new ExperienceType(1L,
                                                            "ExperienceType 1",
                                                            BigDecimal.valueOf(0),
                                                            BigDecimal.valueOf(12),
                                                            BigDecimal.valueOf(4.005));

        ExperienceType experienceType2 = new ExperienceType(2L,
                                                            "ExperienceType 2",
                                                            BigDecimal.valueOf(12),
                                                            BigDecimal.valueOf(10.7989),
                                                            BigDecimal.valueOf(6));

        Experience exp2 = new Experience.Builder()
                .withId(2L)
                .withMember(members.get(0))
                .withName("Experience 2")
                .withEmployer("Employer 2")
                .withPercent(80)
                .withType(experienceType2)
                .withComment("Comment test 2")
                .withStartDate(LocalDate.of(2022, 7, 16))
                .withEndDate(LocalDate.of(2023, 7, 15))
                .build();

        Experience exp3 = new Experience.Builder()
                .withId(3L)
                .withMember(members.get(1))
                .withName("Experience 3")
                .withEmployer("Employer 3")
                .withPercent(60)
                .withType(experienceType1)
                .withComment("Comment test 3")
                .withStartDate(LocalDate.of(2023, 7, 16))
                .withEndDate(LocalDate.of(2024, 7, 15))
                .build();

        List<Calculation> calculations = new ArrayList<>();

        Calculation calc1 = Calculation.Builder
                .builder()
                .withId(1L)
                .withMember(members.get(0))
                .withRole(roles.get(0))
                .withState(CalculationState.DRAFT)
                .withPublicationDate(LocalDate.of(2025, 1, 14))
                .withPublicizedBy("Ldap User")
                .withDegrees(Collections.emptyList())
                .withExperiences(List
                        .of(new ExperienceCalculation(1L, null, exp2, Relevancy.HIGHLY),
                            new ExperienceCalculation(3L, null, exp3, Relevancy.LIMITED)))
                .withCertificates(Collections.emptyList())
                .build();

        calc1.getExperiences().forEach(expCalc -> expCalc.setCalculation(calc1));

        calculations.add(calc1);

        Calculation calc2 = Calculation.Builder
                .builder()
                .withId(2L)
                .withMember(members.get(1))
                .withRole(roles.get(0))
                .withState(CalculationState.ARCHIVED)
                .withPublicationDate(LocalDate.of(2025, 1, 14))
                .withPublicizedBy("Ldap User 2")
                .withDegrees(Collections.emptyList())
                .withExperiences(List.of(new ExperienceCalculation(2L, null, exp2, Relevancy.LITTLE)))
                .withCertificates(Collections.emptyList())
                .build();

        calc2.getExperiences().forEach(expCalc -> expCalc.setCalculation(calc2));

        calculations.add(calc2);

        Calculation calc3 = Calculation.Builder
                .builder()
                .withId(3L)
                .withMember(members.get(1))
                .withRole(roles.get(0))
                .withState(CalculationState.ACTIVE)
                .withDegrees(Collections.emptyList())
                .withExperiences(Collections.emptyList())
                .withCertificates(Collections.emptyList())
                .build();

        calculations.add(calc3);

        return calculations;
    }

    @Override
    @Test
    void shouldDelete() {
        // we don't have a Delete Function because we only update to Archive the
        // calculation
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
