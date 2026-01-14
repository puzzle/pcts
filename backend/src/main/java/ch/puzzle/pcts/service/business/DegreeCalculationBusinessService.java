package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.degreetype.DegreeType;
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

    protected DegreeCalculationBusinessService(DegreeCalculationValidationService validationService,
                                               DegreeCalculationPersistenceService persistenceService) {
        super(validationService, persistenceService);
        this.degreeCalculationPersistenceService = persistenceService;
        this.degreeCalculationValidationService = validationService;
    }

    public List<DegreeCalculation> getByCalculationId(Long calculationId) {
        return this.degreeCalculationPersistenceService.getByCalculationId(calculationId);
    }

    public List<DegreeCalculation> getByDegreeId(Long degreeId) {
        return this.degreeCalculationPersistenceService.getByDegreeId(degreeId);
    }

    public BigDecimal getDegreePoints(Long id) {
        List<DegreeCalculation> degreeCalculations = this.getByCalculationId(id);
        return degreeCalculations.stream().map(this::calculatePoints).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public DegreeCalculation update(Long id, DegreeCalculation degreeCalculation) {
        degreeCalculationValidationService.validateOnUpdate(id, degreeCalculation);
        List<DegreeCalculation> existing = this.getByDegreeId(degreeCalculation.getDegree().getId());
        degreeCalculationValidationService.validateDuplicateDegreeId(degreeCalculation, existing);
        return degreeCalculationPersistenceService.save(degreeCalculation);
    }

    @Override
    public DegreeCalculation create(DegreeCalculation degreeCalculation) {
        degreeCalculationValidationService.validateOnCreate(degreeCalculation);
        List<DegreeCalculation> existing = this.getByDegreeId(degreeCalculation.getDegree().getId());
        degreeCalculationValidationService.validateDuplicateDegreeId(degreeCalculation, existing);
        return degreeCalculationPersistenceService.save(degreeCalculation);
    }

    public List<DegreeCalculation> createDegreeCalculations(Calculation calculation) {
        return calculation.getDegreeCalculations().stream().map(deg -> {
            deg.setCalculation(calculation);
            return this.create(deg);
        }).toList();
    }

    public List<DegreeCalculation> updateDegreeCalculations(Calculation calculation) {
        List<DegreeCalculation> existing = this.getByCalculationId(calculation.getId());

        List<DegreeCalculation> degreeCalculations = calculation.getDegreeCalculations().stream().map(deg -> {
            deg.setCalculation(calculation);
            return deg.getId() == null ? this.create(deg) : this.update(deg.getId(), deg);
        }).toList();

        /*
         * Removing all created or updated degree calculations from the list and then delete the
         * remaining, which are unused
         */
        existing.removeAll(degreeCalculations);
        this.degreeCalculationPersistenceService
                .deleteAllByIdInBatch(existing.stream().map(DegreeCalculation::getId).toList());

        return degreeCalculations;
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
