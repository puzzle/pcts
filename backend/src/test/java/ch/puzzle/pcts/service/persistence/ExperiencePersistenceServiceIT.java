package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.*;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.repository.ExperienceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class ExperiencePersistenceServiceIT
        extends
            PersistenceBaseIT<Experience, ExperienceRepository, ExperiencePersistenceService> {

    @Autowired
    ExperiencePersistenceServiceIT(ExperiencePersistenceService service) {
        super(service);
    }

    @Override
    Experience getModel() {
        return EXPERIENCE_2;
    }

    List<Experience> getAll() {
        return EXPERIENCES;
    }
}