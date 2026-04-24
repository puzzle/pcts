package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.util.TestDataModels.LEADERSHIP_EXPERIENCES;
import static ch.puzzle.pcts.util.TestDataModels.LEADERSHIP_EXPERIENCE_2;

import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.repository.LeadershipExperienceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class LeadershipExperiencePersistenceServiceIT
        extends
            PersistenceBaseIT<LeadershipExperience, LeadershipExperienceRepository, LeadershipExperiencePersistenceService> {

    @Autowired
    LeadershipExperiencePersistenceServiceIT(LeadershipExperiencePersistenceService persistenceService) {
        super(persistenceService);
    }

    @Override
    LeadershipExperience getModel() {
        return LEADERSHIP_EXPERIENCE_2;
    }

    @Override
    List<LeadershipExperience> getAll() {
        return LEADERSHIP_EXPERIENCES;
    }
}
