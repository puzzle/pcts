package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;

class ExperienceTypePersistenceServiceIT
        extends
            PersistenceBaseIT<ExperienceType, ExperienceTypeRepository, ExperienceTypePersistenceService> {

    @Autowired
    ExperienceTypePersistenceServiceIT(ExperienceTypePersistenceService service) {
        super(service);
    }

    @Override
    ExperienceType getCreateEntity() {
        return new ExperienceType(null,
                                  "ExperienceType 3",
                                  BigDecimal.valueOf(10.055),
                                  BigDecimal.valueOf(5.603),
                                  BigDecimal.valueOf(2.005));
    }

    @Override
    ExperienceType getUpdateEntity() {
        return new ExperienceType(null,
                                  "Updated experienceType",
                                  BigDecimal.valueOf(10.055),
                                  BigDecimal.valueOf(5.603),
                                  BigDecimal.valueOf(2.005));
    }

    @Override
    Long getId(ExperienceType experienceType) {
        return experienceType.getId();
    }

    @Override
    void setId(ExperienceType experienceType, Long id) {
        experienceType.setId(id);
    }
}
