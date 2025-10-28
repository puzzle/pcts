package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experience.Experience;
import ch.puzzle.pcts.repository.ExperienceRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperiencePersistenceService extends PersistenceBase<Experience, ExperienceRepository> {
    public ExperiencePersistenceService(ExperienceRepository repository) {
        super(repository);
    }
}
