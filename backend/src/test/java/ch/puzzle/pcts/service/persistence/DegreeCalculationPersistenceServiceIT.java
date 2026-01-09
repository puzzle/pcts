package ch.puzzle.pcts.service.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.repository.DegreeCalculationRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DegreeCalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<DegreeCalculation, DegreeCalculationRepository, DegreeCalculationPersistenceService> {

    CalculationPersistenceServiceIT calculationServiceIT;
    DegreePersistenceServiceIT degreeServiceIT;

    private static final Long DEGREE_CALC_ID_1 = 1L;
    private static final Long DEGREE_CALC_ID_2 = 2L;
    private static final Long DEGREE_CALC_ID_3 = 3L;

    @Autowired
    DegreeCalculationPersistenceServiceIT(DegreeCalculationPersistenceService service,
                                          CalculationPersistenceService calculationService,
                                          DegreePersistenceService degreeService) {
        super(service);
        this.calculationServiceIT = new CalculationPersistenceServiceIT(calculationService, null, null);
        this.degreeServiceIT = new DegreePersistenceServiceIT(degreeService);
    }

    @Override
    DegreeCalculation getModel() {
        Calculation calculation = calculationServiceIT.getAll().getFirst();
        Degree degree = degreeServiceIT.getAll().get(1);

        return new DegreeCalculation(null, calculation, degree, Relevancy.HIGHLY, BigDecimal.valueOf(80), "Comment");
    }

    @Override
    List<DegreeCalculation> getAll() {
        List<Calculation> calculations = calculationServiceIT.getAll();
        Degree degree = degreeServiceIT.getAll().get(1);

        List<DegreeCalculation> list = new ArrayList<>();

        list
                .add(new DegreeCalculation(DEGREE_CALC_ID_1,
                                           calculations.get(0),
                                           degree,
                                           Relevancy.HIGHLY,
                                           BigDecimal.valueOf(80),
                                           "Comment"));

        list
                .add(new DegreeCalculation(DEGREE_CALC_ID_2,
                                           calculations.get(1),
                                           degree,
                                           Relevancy.LITTLE,
                                           BigDecimal.valueOf(10),
                                           "Comment"));

        list
                .add(new DegreeCalculation(DEGREE_CALC_ID_3,
                                           calculations.get(0),
                                           degree,
                                           Relevancy.LIMITED,
                                           BigDecimal.valueOf(100),
                                           "Comment"));

        return list;
    }

    @DisplayName("Should fetch DegreeCalculations by calculationId")
    @Transactional
    @Test
    void shouldGetByCalculationId() {
        List<DegreeCalculation> result = persistenceService.getByCalculationId(1L);

        assertThat(result).hasSize(2).allMatch(dc -> dc.getCalculation().getId().equals(1L));
    }

    @DisplayName("Should fetch DegreeCalculations by degreeId")
    @Transactional
    @Test
    void shouldGetByDegreeId() {
        List<DegreeCalculation> result = persistenceService.getByDegreeId(2L);

        assertThat(result).hasSize(3).allMatch(dc -> dc.getDegree().getId().equals(2L));
    }

    @DisplayName("Should save and retrieve DegreeCalculation correctly")
    @Transactional
    @Test
    void shouldSaveAndRetrieve() {
        DegreeCalculation dc = getModel();
        DegreeCalculation saved = persistenceService.save(dc);

        assertEquals(dc.getCalculation(), saved.getCalculation());
        assertEquals(dc.getDegree(), saved.getDegree());
        assertEquals(dc.getWeight(), saved.getWeight());
        assertEquals(dc.getRelevancy(), saved.getRelevancy());
        assertEquals(dc.getComment(), saved.getComment());

        assertThat(persistenceService.getAll()).contains(saved);
    }
}
