package ch.puzzle.pcts.service.persistence;

import static ch.puzzle.pcts.Constants.LEADERSHIP_EXPERIENCE;

import ch.puzzle.pcts.model.leadershipexperience.LeadershipExperience;
import ch.puzzle.pcts.repository.LeadershipExperienceRepository;
import org.springframework.stereotype.Service;

@Service
public class LeadershipExperiencePersistenceService
        extends
            PersistenceBase<LeadershipExperience, LeadershipExperienceRepository> {

    public LeadershipExperiencePersistenceService(LeadershipExperienceRepository repository) {
        super(repository);
    }

    @Override
    public String entityName() {
        return LEADERSHIP_EXPERIENCE;
    }
}
