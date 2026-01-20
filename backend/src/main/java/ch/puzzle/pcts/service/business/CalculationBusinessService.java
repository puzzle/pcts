package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.certificatecalculation.CertificateCalculation;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.calculation.experiencecalculation.ExperienceCalculation;
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

    public CalculationBusinessService(CalculationValidationService validationService,
                                         CalculationPersistenceService persistenceService,
                                         ExperienceCalculationBusinessService experienceCalculationBusinessService,
                                         CertificateCalculationBusinessService certificateCalculationBusinessService,
                                         DegreeCalculationBusinessService degreeCalculationBusinessService) {
        super(validationService, persistenceService);
        this.experienceCalculationBusinessService = experienceCalculationBusinessService;
        this.certificateCalculationBusinessService = certificateCalculationBusinessService;
        this.degreeCalculationBusinessService = degreeCalculationBusinessService;
    }

    @Override
    @Transactional
    public Calculation create(Calculation calculation) {
        // create base Calculation first to get the final persisted state
        Calculation baseCalc = super.create(calculation);

        // create related entities using the baseCalc
        List<ExperienceCalculation> expCalc = experienceCalculationBusinessService
                .createExperienceCalculations(calculation);
        List<DegreeCalculation> degCalc = degreeCalculationBusinessService
                .createDegreeCalculations(calculation);
        List<CertificateCalculation> certCalc = certificateCalculationBusinessService
                .createCertificateCalculations(calculation);

        // set relations to return a fully-populated object
        baseCalc.setExperienceCalculations(expCalc);
        baseCalc.setDegreeCalculations(degCalc);
        baseCalc.setCertificateCalculations(certCalc);

        return baseCalc;
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

        List<DegreeCalculation> degCalc = degreeCalculationBusinessService
                .updateDegreeCalculations(calculation);

        List<CertificateCalculation> certCalc = certificateCalculationBusinessService
                .updateCertificateCalculations(calculation);

        // set relations to return a fully-populated object
        baseCalc.setExperienceCalculations(expCalc);
        baseCalc.setDegreeCalculations(degCalc);
        baseCalc.setCertificateCalculations(certCalc);

        return baseCalc;
    }

    @Override
    public Calculation getById(Long calculationId) {
        Calculation calculation = super.getById(calculationId);

        BigDecimal totalRelevancyPoints = BigDecimal.ZERO
                .add(experienceCalculationBusinessService.getExperiencePoints(calculationId))
                .add(degreeCalculationBusinessService.getDegreePoints(calculationId))
                .add(certificateCalculationBusinessService.getCertificatePoints(calculationId));

        calculation.setPoints(totalRelevancyPoints);
        return calculation;
    }
}
