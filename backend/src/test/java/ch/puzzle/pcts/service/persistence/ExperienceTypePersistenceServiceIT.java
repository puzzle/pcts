package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import static ch.puzzle.pcts.util.TestDataModels.*;

class ExperienceTypePersistenceServiceIT
        extends
            PersistenceBaseIT<ExperienceType, ExperienceTypeRepository, ExperienceTypePersistenceService> {

    @Autowired
    ExperienceTypePersistenceServiceIT(ExperienceTypePersistenceService service) {
        super(service);
    }

    @Override
    ExperienceType getModel() {
        return EXP_TYPE_2;
    }

    @Override
    List<ExperienceType> getAll() {
        return EXPERIENCE_TYPES;
    }
}
