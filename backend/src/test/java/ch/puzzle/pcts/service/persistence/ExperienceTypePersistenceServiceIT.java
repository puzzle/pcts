package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.EXPERIENCE_TYPES;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

class ExperienceTypePersistenceServiceIT
        extends
            PersistenceBaseIT<ExperienceType, ExperienceTypeRepository, ExperienceTypePersistenceService> {

    @Autowired
    ExperienceTypePersistenceServiceIT(ExperienceTypePersistenceService service) {
        super(service);
    }

    @Override
    ExperienceType getModel() {
        return new ExperienceType(null,
                                  "ExperienceType 3",
                                  BigDecimal.valueOf(10.055),
                                  BigDecimal.valueOf(5.603),
                                  BigDecimal.valueOf(2.005));
    }

    @Override
    List<ExperienceType> getAll() {
        return EXPERIENCE_TYPES;
    }
}
