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
        return ExperienceType.Builder
                .builder()
                .withName("ExperienceType 3")
                .withHighlyRelevantPoints(BigDecimal.valueOf(10.055))
                .withLimitedRelevantPoints(BigDecimal.valueOf(5.603))
                .withLittleRelevantPoints(BigDecimal.valueOf(2.005))
                .build();
    }

    @Override
    List<ExperienceType> getAll() {
        return EXPERIENCE_TYPES;
    }
}
