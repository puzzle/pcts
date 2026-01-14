package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
import ch.puzzle.pcts.model.member.Member;
import ch.puzzle.pcts.model.role.Role;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CalculationBusinessService extends BusinessBase<Calculation> {
    private final ExperienceCalculationBusinessService experienceCalculationBusinessService;
    private final CertificateCalculationBusinessService certificateCalculationBusinessService;
    private final DegreeCalculationBusinessService degreeCalculationBusinessService;
    private final CalculationPersistenceService calculationPersistenceService;

    protected CalculationBusinessService(CalculationValidationService validationService,
                                         CalculationPersistenceService persistenceService,
                                         ExperienceCalculationBusinessService experienceCalculationBusinessService,
                                         CertificateCalculationBusinessService certificateCalculationBusinessService,
                                         DegreeCalculationBusinessService degreeCalculationBusinessService,
                                         CalculationPersistenceService calculationPersistenceService) {
        super(validationService, persistenceService);
        this.experienceCalculationBusinessService = experienceCalculationBusinessService;
        this.certificateCalculationBusinessService = certificateCalculationBusinessService;
        this.degreeCalculationBusinessService = degreeCalculationBusinessService;
        this.calculationPersistenceService = calculationPersistenceService;
    }

    @Override
    @Transactional
    public Calculation create(Calculation calculation) {
        // create base Calculation first to get the final persisted state
        Calculation createdCalculation = super.create(calculation);

        // create related entities using the createdCalculation
        List<ExperienceCalculation> createdExperienceCalculations = experienceCalculationBusinessService
                .createExperienceCalculations(calculation);
        List<DegreeCalculation> createdDegreeCalculations = degreeCalculationBusinessService
                .createDegreeCalculations(calculation);
        List<CertificateCalculation> createdCertificatesCalculations = certificateCalculationBusinessService
                .createCertificateCalculations(calculation);

        // set relations to return a fully-populated object
        createdCalculation.setExperienceCalculations(createdExperienceCalculations);
        createdCalculation.setDegreeCalculations(createdDegreeCalculations);
        createdCalculation.setCertificateCalculations(createdCertificatesCalculations);

        return createdCalculation;
    }

    @Override
    @Transactional
    public Calculation update(Long id, Calculation calculation) {
        // update base Calculation first to get the final persisted state
        Calculation updatedCalculation = super.update(id, calculation);

        // update related entities using the updatedCalculation
        List<ExperienceCalculation> updatedExperienceCalculations = experienceCalculationBusinessService
                .updateExperienceCalculations(updatedCalculation);

        List<DegreeCalculation> updatedDegreeCalculations = degreeCalculationBusinessService
                .updateDegreeCalculations(updatedCalculation);

        List<CertificateCalculation> updatedCertificateCalculations = certificateCalculationBusinessService
                .updateCertificateCalculations(updatedCalculation);

        // set relations to return a fully-populated object
        updatedCalculation.setExperienceCalculations(updatedExperienceCalculations);
        updatedCalculation.setDegreeCalculations(updatedDegreeCalculations);
        updatedCalculation.setCertificateCalculations(updatedCertificateCalculations);

        return updatedCalculation;
    }

    @Override
    public Calculation getById(Long calculationId) {
        Calculation calculation = super.getById(calculationId);
        calculation.setPoints(getPointsOfCalculation(calculation));
        return calculation;
    }

    public List<Calculation> getAllByMember(Member member) {
        List<Calculation> calculations = calculationPersistenceService.getAllByMember(member);
        setPointsForCalculations(calculations);
        return calculations;
    }

    public List<Calculation> getAllByMemberAndRole(Member member, Role role) {
        List<Calculation> calculations = calculationPersistenceService.getAllByMemberAndRole(member, role);
        setPointsForCalculations(calculations);
        return calculations;
    }

    private BigDecimal getPointsOfCalculation(Calculation calculation) {
        Long calculationId = calculation.getId();
        return BigDecimal.ZERO
                .add(experienceCalculationBusinessService.getExperiencePoints(calculationId))
                .add(degreeCalculationBusinessService.getDegreePoints(calculationId))
                .add(certificateCalculationBusinessService.getCertificatePoints(calculationId));
    }

    private void setPointsForCalculations(List<Calculation> calculations) {
        calculations.forEach(calculation -> {
            BigDecimal points = this.getPointsOfCalculation(calculation);
            calculation.setPoints(points);
        });
    }
}
