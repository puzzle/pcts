package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.certificate.Certificate;
import ch.puzzle.pcts.model.certificatetype.CertificateKind;
import ch.puzzle.pcts.model.certificatetype.CertificateType;
import ch.puzzle.pcts.model.certificatetype.Tag;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
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
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<Calculation, CalculationRepository, CalculationPersistenceService> {

    MemberPersistenceServiceIT memberPersistenceServiceIT;
    RolePersistenceServiceIT rolePersistenceServiceIT;

    private static final Long CALCULATION_ID_1 = 1L;
    private static final Long CALCULATION_ID_2 = 2L;
    private static final Long CALCULATION_ID_3 = 3L;

    private static final Long EXPERIENCE_TYPE_ID_1 = 1L;
    private static final Long EXPERIENCE_TYPE_ID_2 = 2L;
    private static final Long EXPERIENCE_ID_2 = 2L;
    private static final Long EXPERIENCE_ID_3 = 3L;

    private static final Long DEGREE_TYPE_ID_2 = 2L;
    private static final Long DEGREE_ID_2 = 2L;

    private static final Long CERTIFICATE_TYPE_ID_2 = 2L;
    private static final Long CERTIFICATE_TYPE_ID_5 = 5L;
    private static final Long CERTIFICATE_ID_2 = 2L;
    private static final Long CERTIFICATE_ID_5 = 5L;

    private static final Long DEGREE_CALC_ID_1 = 1L;
    private static final Long DEGREE_CALC_ID_2 = 2L;
    private static final Long DEGREE_CALC_ID_3 = 3L;

    private static final Long EXPERIENCE_CALC_ID_1 = 1L;
    private static final Long EXPERIENCE_CALC_ID_2 = 2L;
    private static final Long EXPERIENCE_CALC_ID_3 = 3L;

    private static final Long CERTIFICATE_CALC_ID_1 = 1L;
    private static final Long CERTIFICATE_CALC_ID_2 = 2L;
    private static final Long CERTIFICATE_CALC_ID_3 = 3L;

    @Autowired
    CalculationPersistenceServiceIT(CalculationPersistenceService service,
                                    MemberPersistenceService memberPersistenceService,
                                    RolePersistenceService rolePersistenceService) {
        super(service);
        this.memberPersistenceServiceIT = new MemberPersistenceServiceIT(memberPersistenceService);
        this.rolePersistenceServiceIT = new RolePersistenceServiceIT(rolePersistenceService);
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

        ExperienceType experienceType1 = new ExperienceType(EXPERIENCE_TYPE_ID_1,
                                                            "ExperienceType 1",
                                                            BigDecimal.valueOf(0),
                                                            BigDecimal.valueOf(12),
                                                            BigDecimal.valueOf(4.005));

        ExperienceType experienceType2 = new ExperienceType(EXPERIENCE_TYPE_ID_2,
                                                            "ExperienceType 2",
                                                            BigDecimal.valueOf(12),
                                                            BigDecimal.valueOf(10.7989),
                                                            BigDecimal.valueOf(6));

        Experience exp2 = new Experience.Builder()
                .withId(EXPERIENCE_ID_2)
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
                .withId(EXPERIENCE_ID_3)
                .withMember(members.get(1))
                .withName("Experience 3")
                .withEmployer("Employer 3")
                .withPercent(60)
                .withType(experienceType1)
                .withComment("Comment test 3")
                .withStartDate(LocalDate.of(2023, 7, 16))
                .withEndDate(LocalDate.of(2024, 7, 15))
                .build();

        DegreeType degreeType2 = new DegreeType(DEGREE_TYPE_ID_2,
                                                "Degree type 2",
                                                BigDecimal.valueOf(12),
                                                BigDecimal.valueOf(3.961),
                                                BigDecimal.valueOf(3));

        Degree degree2 = Degree.Builder
                .builder()
                .withId(DEGREE_ID_2)
                .withMember(members.get(1))
                .withName("Degree 2")
                .withInstitution("Institution")
                .withCompleted(false)
                .withDegreeType(degreeType2)
                .withStartDate(LocalDate.of(2016, 9, 1))
                .withEndDate(LocalDate.of(2019, 6, 30))
                .withComment("Comment")
                .build();

        CertificateType certType2 = new CertificateType(CERTIFICATE_TYPE_ID_2,
                                                        "Certificate Type 2",
                                                        BigDecimal.valueOf(1),
                                                        "This is Certificate 2",
                                                        Set.of(new Tag(2L, "Longer tag name")),
                                                        CertificateKind.CERTIFICATE);

        CertificateType certType5 = new CertificateType(CERTIFICATE_TYPE_ID_5,
                                                        "LeadershipExperience Type 1",
                                                        BigDecimal.valueOf(5.5),
                                                        "This is LeadershipExperience 1",
                                                        Set.of(),
                                                        CertificateKind.MILITARY_FUNCTION);

        Certificate cert2 = Certificate.Builder
                .builder()
                .withId(CERTIFICATE_ID_2)
                .withMember(members.get(1))
                .withCertificateType(certType2)
                .withCompletedAt(LocalDate.of(2022, 11, 1))
                .withComment("Completed first aid training.")
                .build();

        Certificate cert5 = Certificate.Builder
                .builder()
                .withId(CERTIFICATE_ID_5)
                .withMember(members.get(0))
                .withCertificateType(certType5)
                .withCompletedAt(LocalDate.of(2010, 8, 12))
                .withValidUntil(LocalDate.of(2023, 3, 25))
                .withComment("Left organization.")
                .build();

        List<Calculation> calculations = new ArrayList<>();

        Calculation calc1 = Calculation.Builder
                .builder()
                .withId(CALCULATION_ID_1)
                .withMember(members.get(0))
                .withRole(roles.get(0))
                .withState(CalculationState.DRAFT)
                .withPublicationDate(LocalDate.of(2025, 1, 14))
                .withPublicizedBy("Ldap User")
                .withDegrees(List
                        .of(new DegreeCalculation(DEGREE_CALC_ID_1,
                                                  null,
                                                  degree2,
                                                  Relevancy.HIGHLY,
                                                  BigDecimal.valueOf(80),
                                                  "Comment"),
                            new DegreeCalculation(DEGREE_CALC_ID_3,
                                                  null,
                                                  degree2,
                                                  Relevancy.LIMITED,
                                                  BigDecimal.valueOf(100),
                                                  "Comment")))
                .withExperiences(List
                        .of(new ExperienceCalculation(EXPERIENCE_CALC_ID_1, null, exp2, Relevancy.HIGHLY, "Comment"),
                            new ExperienceCalculation(EXPERIENCE_CALC_ID_3, null, exp3, Relevancy.LIMITED, "Comment")))
                .withCertificates(List
                        .of(new CertificateCalculation(CERTIFICATE_CALC_ID_1, null, cert2),
                            new CertificateCalculation(CERTIFICATE_CALC_ID_3, null, cert5)))
                .build();

        calc1.getExperiences().forEach(e -> e.setCalculation(calc1));
        calc1.getDegrees().forEach(d -> d.setCalculation(calc1));
        calc1.getCertificates().forEach(c -> c.setCalculation(calc1));

        calculations.add(calc1);

        Calculation calc2 = Calculation.Builder
                .builder()
                .withId(CALCULATION_ID_2)
                .withMember(members.get(1))
                .withRole(roles.get(0))
                .withState(CalculationState.ARCHIVED)
                .withPublicationDate(LocalDate.of(2025, 1, 14))
                .withPublicizedBy("Ldap User 2")
                .withDegrees(List
                        .of(new DegreeCalculation(DEGREE_CALC_ID_2,
                                                  null,
                                                  degree2,
                                                  Relevancy.LITTLE,
                                                  BigDecimal.valueOf(10),
                                                  "Comment")))
                .withExperiences(List
                        .of(new ExperienceCalculation(EXPERIENCE_CALC_ID_2, null, exp2, Relevancy.LITTLE, "Comment")))
                .withCertificates(List.of(new CertificateCalculation(CERTIFICATE_CALC_ID_2, null, cert2)))
                .build();

        calc2.getExperiences().forEach(e -> e.setCalculation(calc2));
        calc2.getDegrees().forEach(d -> d.setCalculation(calc2));
        calc2.getCertificates().forEach(c -> c.setCalculation(calc2));

        calculations.add(calc2);

        Calculation calc3 = Calculation.Builder
                .builder()
                .withId(CALCULATION_ID_3)
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

        oldActiveCalculation.setId(CALCULATION_ID_2);
        service.save(oldActiveCalculation);

        activeCalculation.setId(CALCULATION_ID_3);
        Calculation result = service.save(activeCalculation);

        assertEquals(LocalDate.now(), result.getPublicationDate());
        assertEquals("Ldap User", result.getPublicizedBy());
        assertThat(getActiveCalculationsOfMember(activeCalculation.getRole(), activeCalculation.getMember()))
                .containsExactly(result);
    }

    private List<Calculation> getActiveCalculationsOfMember(Role role, Member member) {
        return service
                .getAll()
                .stream()
                .filter(c -> c.getState() == CalculationState.ACTIVE && c.getRole().equals(role)
                             && c.getMember().equals(member))
                .toList();
    }
}
