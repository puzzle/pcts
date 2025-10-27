package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExperienceTypePersistenceService extends PersistenceBase<ExperienceType, ExperienceTypeRepository> {

    private final ExperienceTypeRepository repository;

    public ExperienceTypePersistenceService(ExperienceTypeRepository repository) {
        super(repository);
        this.repository = repository;
    }
    public Optional<ExperienceType> getByName(String name) {
        return repository.findByName(name);
    }
}
