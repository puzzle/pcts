package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.DEGREE_TYPES;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

class DegreeTypePersistenceServiceIT
        extends
            PersistenceBaseIT<DegreeType, DegreeTypeRepository, DegreeTypePersistenceService> {

    @Autowired
    DegreeTypePersistenceServiceIT(DegreeTypePersistenceService service) {
        super(service);
    }

    @Override
    DegreeType getModel() {
        return DegreeType.Builder
                .builder()
                .withName("DegreeTypes 3")
                .withHighlyRelevantPoints(BigDecimal.valueOf(10.055))
                .withLimitedRelevantPoints(BigDecimal.valueOf(5.603))
                .withLittleRelevantPoints(BigDecimal.valueOf(2.005))
                .build();
    }

    @Override
    List<DegreeType> getAll() {
        return DEGREE_TYPES;
    }
}