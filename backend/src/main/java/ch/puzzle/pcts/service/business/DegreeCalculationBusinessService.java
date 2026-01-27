package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.service.persistence.DegreeCalculationPersistenceService;
import ch.puzzle.pcts.service.validation.DegreeCalculationValidationService;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DegreeCalculationBusinessService extends BusinessBase<DegreeCalculation> {
    private final DegreeCalculationPersistenceService degreeCalculationPersistenceService;
    private final DegreeCalculationValidationService degreeCalculationValidationService;

    public DegreeCalculationBusinessService(DegreeCalculationValidationService validationService,
                                            DegreeCalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
        this.degreeCalculationPersistenceService = persistenceService;
        this.degreeCalculationValidationService = validationService;
    }

    public List<DegreeCalculation> getByCalculationId(Long calculationId) {
        return degreeCalculationPersistenceService.getByCalculationId(calculationId);
    }

    public List<DegreeCalculation> getByDegreeId(Long degreeId) {
        return degreeCalculationPersistenceService.getByDegreeId(degreeId);
    }

    public BigDecimal getDegreePoints(Long id) {
        List<DegreeCalculation> degreeCalculations = getByCalculationId(id);

        return degreeCalculations
                .stream()
                .filter(dc -> dc.getDegree().getCompleted())
                .map(this::calculatePoints)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public DegreeCalculation update(Long id, DegreeCalculation degreeCalculation) {
        degreeCalculationValidationService.validateOnUpdate(id, degreeCalculation);
        List<DegreeCalculation> existing = getByDegreeId(degreeCalculation.getDegree().getId());
        degreeCalculationValidationService.validateDuplicateDegreeId(degreeCalculation, existing);
        return degreeCalculationPersistenceService.save(degreeCalculation);
    }

    @Override
    public DegreeCalculation create(DegreeCalculation degreeCalculation) {
        degreeCalculationValidationService.validateOnCreate(degreeCalculation);
        List<DegreeCalculation> existing = getByDegreeId(degreeCalculation.getDegree().getId());
        degreeCalculationValidationService.validateDuplicateDegreeId(degreeCalculation, existing);
        return degreeCalculationPersistenceService.save(degreeCalculation);
    }

    public List<DegreeCalculation> createDegreeCalculations(Calculation calculation) {
        return calculation.getDegreeCalculations().stream().map(deg -> {
            deg.setCalculation(calculation);
            return create(deg);
        }).toList();
    }

    public List<DegreeCalculation> updateDegreeCalculations(Calculation calculation) {
        List<DegreeCalculation> existing = getByCalculationId(calculation.getId());

        List<DegreeCalculation> degreeCalculations = calculation.getDegreeCalculations().stream().map(deg -> {
            deg.setCalculation(calculation);
            return deg.getId() == null ? create(deg) : update(deg.getId(), deg);
        }).toList();
        deleteUnusedDegreeCalculations(existing, degreeCalculations);

        return degreeCalculations;
    }

    private void deleteUnusedDegreeCalculations(List<DegreeCalculation> existing, List<DegreeCalculation> updated) {
        // remove all created/updated degree calculations from the list
        existing.removeAll(updated);

        // delete the remaining, which are unused
        degreeCalculationPersistenceService
                .deleteAllByIdInBatch(existing.stream().map(DegreeCalculation::getId).toList());
    }

    /*
     * The divisions are rounded to a DECIMAL128 digit because numbers with a
     * infinite amount of digits could cause a ArithmeticException
     */
    private BigDecimal calculatePoints(DegreeCalculation calculation) {
        Relevancy relevancy = calculation.getRelevancy();
        BigDecimal weight = calculation.getWeight();

        BigDecimal pointsByRelevancy = calculation.getDegree().getDegreeType().getPointsByRelevancy(relevancy);

        return pointsByRelevancy.divide(BigDecimal.valueOf(100), MathContext.DECIMAL128).multiply(weight);
    }
}
