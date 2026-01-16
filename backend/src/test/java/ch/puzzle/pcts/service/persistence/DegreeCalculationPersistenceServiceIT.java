package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestData.*;
import static ch.puzzle.pcts.util.TestDataModels.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.pcts.model.calculation.Relevancy;
import ch.puzzle.pcts.model.calculation.degreecalculation.DegreeCalculation;
import ch.puzzle.pcts.repository.DegreeCalculationRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DegreeCalculationPersistenceServiceIT
        extends
            PersistenceBaseIT<DegreeCalculation, DegreeCalculationRepository, DegreeCalculationPersistenceService> {

    @Autowired
    DegreeCalculationPersistenceServiceIT(DegreeCalculationPersistenceService service) {
        super(service);
    }

    @Override
    DegreeCalculation getModel() {
        return new DegreeCalculation(null,
                                     CALCULATION_1,
                                     DEGREE_2,
                                     Relevancy.HIGHLY,
                                     BigDecimal.valueOf(80),
                                     "Comment");
    }

    @Override
    List<DegreeCalculation> getAll() {
        return DEGREE_CALCULATIONS;
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

    @DisplayName("Should delete all DegreeCalculations by IDs in batch")
    @Transactional
    @Test
    void shouldDeleteAllByIdInBatch() {
        DegreeCalculation dc1 = persistenceService.save(getAll().get(0));
        DegreeCalculation dc2 = persistenceService.save(getAll().get(1));
        DegreeCalculation dc3 = persistenceService.save(getAll().get(2));

        List<Long> idsToDelete = List.of(dc1.getId(), dc3.getId());

        persistenceService.deleteAllByIdInBatch(idsToDelete);

        List<DegreeCalculation> remaining = persistenceService.getAll();

        assertThat(remaining).hasSize(1);
        assertThat(remaining.getFirst().getId()).isEqualTo(dc2.getId());
    }

}
