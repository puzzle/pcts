package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.calculation.leadershipexperiencecalculation.LeadershipExperienceCalculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculationBusinessService extends BusinessBase<Calculation> {
    private final ExperienceCalculationBusinessService experienceCalculationBusinessService;
    private final CertificateCalculationBusinessService certificateCalculationBusinessService;
    private final LeadershipExperienceCalculationBusinessService leadershipExperienceCalculationBusinessService;
    private final DegreeCalculationBusinessService degreeCalculationBusinessService;
    private final CalculationPersistenceService calculationPersistenceService;
    private final MemberBusinessService memberBusinessService;
    private final DegreeBusinessService degreeBusinessService;
    private final CertificateBusinessService certificateBusinessService;
    private final LeadershipExperienceBusinessService leadershipExperienceBusinessService;
    private final ExperienceBusinessService experienceBusinessService;
    private final RoleBusinessService roleBusinessService;

    public CalculationBusinessService(CalculationValidationService validationService,
                                      CalculationPersistenceService persistenceService,
                                      ExperienceCalculationBusinessService experienceCalculationBusinessService,
                                      CertificateCalculationBusinessService certificateCalculationBusinessService,
                                      LeadershipExperienceCalculationBusinessService leadershipExperienceCalculationBusinessService,
                                      DegreeCalculationBusinessService degreeCalculationBusinessService,
                                      MemberBusinessService memberBusinessService,
                                      DegreeBusinessService degreeBusinessService,
                                      CertificateBusinessService certificateBusinessService,
                                      LeadershipExperienceBusinessService leadershipExperienceBusinessService,
                                      ExperienceBusinessService experienceBusinessService,
                                      RoleBusinessService roleBusinessService) {
        super(validationService, persistenceService);
        this.experienceCalculationBusinessService = experienceCalculationBusinessService;
        this.certificateCalculationBusinessService = certificateCalculationBusinessService;
        this.degreeCalculationBusinessService = degreeCalculationBusinessService;
        this.leadershipExperienceCalculationBusinessService = leadershipExperienceCalculationBusinessService;
        this.calculationPersistenceService = persistenceService;
        this.memberBusinessService = memberBusinessService;
        this.degreeBusinessService = degreeBusinessService;
        this.certificateBusinessService = certificateBusinessService;
        this.leadershipExperienceBusinessService = leadershipExperienceBusinessService;
        this.experienceBusinessService = experienceBusinessService;
        this.roleBusinessService = roleBusinessService;
    }

    @Override
    @Transactional
    public Calculation create(Calculation calculation) {
        // create base Calculation first to get the final persisted state
        Calculation baseCalc = super.create(calculation);

        // create related entities using the baseCalc
        List<ExperienceCalculation> expCalc = experienceCalculationBusinessService
                .createExperienceCalculations(calculation);
        List<DegreeCalculation> degCalc = degreeCalculationBusinessService.createDegreeCalculations(calculation);
        List<CertificateCalculation> certCalc = certificateCalculationBusinessService
                .createCertificateCalculations(calculation);
        List<LeadershipExperienceCalculation> leadershipExpCalc = leadershipExperienceCalculationBusinessService
                .createLeadershipExperienceCalculations(calculation);

        // set relations to return a fully-populated object
        baseCalc.setExperienceCalculations(expCalc);
        baseCalc.setDegreeCalculations(degCalc);
        baseCalc.setCertificateCalculations(certCalc);
        baseCalc.setLeadershipExperienceCalculations(leadershipExpCalc);
        baseCalc.setPoints(getPointsOfCalculation(calculation));

        return sortCalculationsByEndDate(baseCalc);
    }

    @Override
    @Transactional
    public Calculation update(Long id, Calculation calculation) {
        // update base Calculation first to get the final persisted state
        Calculation baseCalc = super.update(id, calculation);
        calculation.setId(baseCalc.getId());

        // update related entities using the baseCalc we use the not persisted
        // calculation for this as the persisted calculation does not contain any
        // relations yet
        List<ExperienceCalculation> expCalc = experienceCalculationBusinessService
                .updateExperienceCalculations(calculation);

        List<DegreeCalculation> degCalc = degreeCalculationBusinessService.updateDegreeCalculations(calculation);

        List<CertificateCalculation> certCalc = certificateCalculationBusinessService
                .updateCertificateCalculations(calculation);

        List<LeadershipExperienceCalculation> leadershipExpCalc = leadershipExperienceCalculationBusinessService
                .updateLeadershipExperienceCalculations(calculation);

        // set relations to return a fully-populated object
        baseCalc.setExperienceCalculations(expCalc);
        baseCalc.setDegreeCalculations(degCalc);
        baseCalc.setCertificateCalculations(certCalc);
        baseCalc.setLeadershipExperienceCalculations(leadershipExpCalc);
        baseCalc.setPoints(getPointsOfCalculation(calculation));

        return sortCalculationsByEndDate(baseCalc);
    }

    @Override
    public Calculation getById(Long calculationId) {
        Calculation calculation = super.getById(calculationId);
        calculation.setPoints(getPointsOfCalculation(calculation));
        return sortCalculationsByEndDate(calculation);
    }

    public List<Calculation> getAllByMember(Member member) {
        List<Calculation> calculations = calculationPersistenceService.getAllByMember(member);
        setPointsForCalculations(calculations);
        return calculations.stream().map(CalculationBusinessService::sortCalculationsByEndDate).toList();
    }

    public List<Calculation> getAllByMemberAndState(Member member, CalculationState state) {
        List<Calculation> calculations = calculationPersistenceService.getAllByMemberAndState(member, state);
        setPointsForCalculations(calculations);
        return calculations.stream().map(CalculationBusinessService::sortCalculationsByEndDate).toList();
    }

    public List<Calculation> getAllByMemberAndRole(Member member, Role role) {
        List<Calculation> calculations = calculationPersistenceService.getAllByMemberAndRole(member, role);
        setPointsForCalculations(calculations);
        return calculations.stream().map(CalculationBusinessService::sortCalculationsByEndDate).toList();
    }

    private BigDecimal getPointsOfCalculation(Calculation calculation) {
        Long calculationId = calculation.getId();
        return BigDecimal.ZERO
                .add(experienceCalculationBusinessService.getExperiencePoints(calculationId))
                .add(degreeCalculationBusinessService.getDegreePoints(calculationId))
                .add(certificateCalculationBusinessService.getCertificatePoints(calculationId))
                .add(leadershipExperienceCalculationBusinessService.getLeadershipExperiencePoints(calculationId));
    }

    private void setPointsForCalculations(List<Calculation> calculations) {
        calculations.forEach(calculation -> {
            BigDecimal points = this.getPointsOfCalculation(calculation);
            calculation.setPoints(points);
        });
    }

    public Calculation getCalculationTemplate(Long memberId, Long roleId) {
        Member member = memberBusinessService.getById(memberId);
        Role role = roleBusinessService.getById(roleId);

        Calculation calculation = getCalculationTemplateForMemberAndRole(member, role);

        calculation.setPoints(getPointsOfCalculation(calculation));

        return calculation;
    }

    private Calculation getCalculationTemplateForMemberAndRole(Member member, Role role) {
        List<DegreeCalculation> degreeCalculations = degreeBusinessService
                .findAllByMember(member)
                .stream()
                .map(degree -> DegreeCalculation.Builder.builder().withDegree(degree).build())
                .toList();

        List<ExperienceCalculation> experienceCalculations = experienceBusinessService
                .findAllByMember(member)
                .stream()
                .map(experience -> ExperienceCalculation.Builder.builder().withExperience(experience).build())
                .toList();

        List<LeadershipExperienceCalculation> leadershipCalculations = new ArrayList<>();

        if (role.getIsManagement()) {
            leadershipCalculations = leadershipExperienceBusinessService
                    .findAllByMember(member)
                    .stream()
                    .map(leadershipExperience -> LeadershipExperienceCalculation.Builder
                            .builder()
                            .withLeadershipExperience(leadershipExperience)
                            .build())
                    .toList();
        }

        List<CertificateCalculation> certificateCalculations = certificateBusinessService
                .findAllByMember(member)
                .stream()
                .map(certificate -> CertificateCalculation.Builder.builder().withCertificate(certificate).build())
                .toList();

        return Calculation.Builder
                .builder()
                .withId(null)
                .withMember(member)
                .withRole(role)
                .withState(CalculationState.DRAFT)
                .withPublicationDate(null)
                .withPublicizedBy(null)
                .withPoints(BigDecimal.ZERO)
                .withCertificateCalculations(certificateCalculations)
                .withLeadershipExperienceCalculations(leadershipCalculations)
                .withDegreeCalculations(degreeCalculations)
                .withExperienceCalculations(experienceCalculations)
                .build();
    }

    public static Calculation sortCalculationsByEndDate(Calculation calculation) {
        if (calculation == null) {
            return null;
        }

        List<CertificateCalculation> sortedCertificates = calculation.getCertificateCalculations() == null ? null
                : calculation
                .getCertificateCalculations()
                .stream()
                .sorted(Comparator.comparing(CalculationBusinessService::getCertificateDate))
                .toList();

        List<DegreeCalculation> sortedDegrees = calculation.getDegreeCalculations() == null ? null
                : calculation
                .getDegreeCalculations()
                .stream()
                .sorted(Comparator.comparing(CalculationBusinessService::getDegreeDate))
                .toList();

        List<ExperienceCalculation> sortedExperiences = calculation.getExperienceCalculations() == null ? null
                : calculation
                .getExperienceCalculations()
                .stream()
                .sorted(Comparator.comparing(CalculationBusinessService::getExperienceDate))
                .toList();

        return Calculation.Builder.builder()
                .withId(calculation.getId())
                .withMember(calculation.getMember())
                .withRole(calculation.getRole())
                .withState(calculation.getState())
                .withPublicationDate(calculation.getPublicationDate())
                .withPublicizedBy(calculation.getPublicizedBy())
                .withPoints(calculation.getPoints())
                .withCertificateCalculations(sortedCertificates)
                .withDegreeCalculations(sortedDegrees)
                .withExperienceCalculations(sortedExperiences)
                .withLeadershipExperienceCalculations(calculation.getLeadershipExperienceCalculations())
                .build();
    }

    private static LocalDate getCertificateDate(CertificateCalculation c) {
        return (c != null && c.getCertificate() != null) ? c.getCertificate().getCompletedAt() : null;
    }

    private static LocalDate getDegreeDate(DegreeCalculation d) {
        return (d != null && d.getDegree() != null) ? d.getDegree().getEndDate() : null;
    }

    private static LocalDate getExperienceDate(ExperienceCalculation e) {
        return (e != null && e.getExperience() != null) ? e.getExperience().getEndDate() : null;
    }

}
