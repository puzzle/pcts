package ch.puzzle.pcts.service.persistence;

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
    List<ExperienceType> getAll() {
        return List
                .of(new ExperienceType(1L,
                                       "ExperienceType 1",
                                       BigDecimal.valueOf(0),
                                       BigDecimal.valueOf(12),
                                       BigDecimal.valueOf(4.005)),
                    new ExperienceType(2L,
                                       "ExperienceType 2",
                                       BigDecimal.valueOf(12),
                                       BigDecimal.valueOf(10.7989),
                                       BigDecimal.valueOf(6)));
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
