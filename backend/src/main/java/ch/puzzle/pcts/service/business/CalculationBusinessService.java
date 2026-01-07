package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class CalculationBusinessService extends BusinessBase<Calculation> {
    private final ExperienceCalculationBusinessService experienceCalculationBusinessService;
    private final CertificateCalculationBusinessService certificateCalculationBusinessService;
    private final DegreeCalculationBusinessService degreeCalculationBusinessService;

    protected CalculationBusinessService(CalculationValidationService validationService,
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
        Calculation createdCalculation = super.create(calculation);

        createdCalculation
                .setExperiences(experienceCalculationBusinessService.createExperienceCalculations(calculation));
        createdCalculation.setDegrees(degreeCalculationBusinessService.createDegreeCalculations(calculation));

        createdCalculation
                .setCertificates(certificateCalculationBusinessService.createCertificateCalculations(calculation));

        return createdCalculation;
    }

    @Override
    @Transactional
    public Calculation update(Long id, Calculation calculation) {
        Calculation updatedCalculation = super.update(id, calculation);

        updatedCalculation
                .setExperiences(experienceCalculationBusinessService.updateExperienceCalculations(calculation));
        updatedCalculation.setDegrees(degreeCalculationBusinessService.updateDegreeCalculations(calculation));
        updatedCalculation
                .setCertificates(certificateCalculationBusinessService.updateCertificateCalculations(calculation));

        return updatedCalculation;
    }

    @Override
    public Calculation getById(Long id) {
        Calculation calculation = super.getById(id);

        BigDecimal totalRelevancyPoints = BigDecimal.ZERO;

        totalRelevancyPoints = totalRelevancyPoints.add(experienceCalculationBusinessService.getExperiencePoints(id));

        totalRelevancyPoints = totalRelevancyPoints.add(degreeCalculationBusinessService.getDegreePoints(id));

        totalRelevancyPoints = totalRelevancyPoints.add(certificateCalculationBusinessService.getCertificatePoints(id));

        calculation.setPoints(totalRelevancyPoints);
        return calculation;
    }
}
