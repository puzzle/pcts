package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypePersistenceService extends PersistenceBase<ExperienceType, ExperienceTypeRepository> {
    public ExperienceTypePersistenceService(ExperienceTypeRepository repository) {
        super(repository);
    }
}
