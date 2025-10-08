package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import java.math.BigDecimal;

class DegreeTypePersistenceServiceIT
        extends
            PersistenceBaseIT<DegreeType, DegreeTypeRepository, DegreeTypePersistenceService> {

    DegreeTypePersistenceServiceIT(DegreeTypePersistenceService service) {
        super(service);
    }

    @Override
    DegreeType getCreateEntity() {
        return new DegreeType(null,
                              "DegreeTypes 3",
                              BigDecimal.valueOf(10.055),
                              BigDecimal.valueOf(5.603),
                              BigDecimal.valueOf(2.005));
    }

    @Override
    DegreeType getUpdateEntity() {
        return new DegreeType(null,
                              "Updated degreeType",
                              BigDecimal.valueOf(10.055),
                              BigDecimal.valueOf(5.603),
                              BigDecimal.valueOf(2.005));
    }

    @Override
    Long getId(DegreeType degreeType) {
        return degreeType.getId();
    }

    @Override
    void setId(DegreeType degreeType, Long id) {
        degreeType.setId(id);
    }
}