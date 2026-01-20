package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.DEGREE_TYPES;
import static ch.puzzle.pcts.util.TestDataModels.DEGREE_TYPE_2;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
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
        return DEGREE_TYPE_2;
    }

    @Override
    List<DegreeType> getAll() {
        return DEGREE_TYPES;
    }
}