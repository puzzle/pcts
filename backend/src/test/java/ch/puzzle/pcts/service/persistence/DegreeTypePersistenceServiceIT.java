package ch.puzzle.pcts.service.persistence;

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
    List<DegreeType> getAll() {
        return List
                .of(new DegreeType(1L,
                                   "Degree type 1",
                                   BigDecimal.valueOf(120.55),
                                   BigDecimal.valueOf(60),
                                   BigDecimal.valueOf(15.45)),
                    new DegreeType(2L,
                                   "Degree type 2",
                                   BigDecimal.valueOf(12),
                                   BigDecimal.valueOf(3.961),
                                   BigDecimal.valueOf(3)));
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