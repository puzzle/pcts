package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import ch.puzzle.pcts.service.validation.CalculationValidationService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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

    @Transactional
    public Calculation create(Calculation calculation) {
        validationService.validateOnCreate(calculation);

        Calculation createdCalculation = persistenceService.save(calculation);

        createdCalculation
                .setExperiences(experienceCalculationBusinessService.createExperienceCalculations(calculation));
        createdCalculation.setDegrees(degreeCalculationBusinessService.createDegreeCalculations(calculation));

        createdCalculation
                .setCertificates(certificateCalculationBusinessService.createCertificateCalculations(calculation));

        return createdCalculation;
    }

    @Transactional
    public Calculation update(Long id, Calculation calculation) {
        if (persistenceService.getById(id).isEmpty()) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "id", FieldKey.IS, id.toString());
            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);
            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        }

        validationService.validateOnUpdate(id, calculation);
        calculation.setId(id);

        Calculation updatedCalculation = persistenceService.save(calculation);

        updatedCalculation
                .setExperiences(experienceCalculationBusinessService.updateExperienceCalculations(calculation));
        updatedCalculation.setDegrees(degreeCalculationBusinessService.updateDegreeCalculations(calculation));
        updatedCalculation
                .setCertificates(certificateCalculationBusinessService.updateCertificateCalculations(calculation));

        return updatedCalculation;
    }

    public Calculation getById(Long id) {
        Calculation calculation = persistenceService.getById(id).orElseThrow(() -> {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "id", FieldKey.IS, id.toString());
            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);
            return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        });

        BigDecimal totalRelevancyPoints = BigDecimal.ZERO;

        totalRelevancyPoints = totalRelevancyPoints.add(experienceCalculationBusinessService.getExperiencePoints(id));

        totalRelevancyPoints = totalRelevancyPoints.add(degreeCalculationBusinessService.getDegreePoints(id));

        totalRelevancyPoints = totalRelevancyPoints.add(certificateCalculationBusinessService.getCertificatePoints(id));

        calculation.setPoints(totalRelevancyPoints);
        return calculation;
    }

    @Override
    protected String entityName() {
        return CALCULATION;
    }
}
