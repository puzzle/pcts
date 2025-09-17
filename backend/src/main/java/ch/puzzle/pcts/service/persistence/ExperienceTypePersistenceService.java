package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experienceType.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExperienceTypePersistenceService {
    private final ExperienceTypeRepository repository;

    @Autowired
    public ExperienceTypePersistenceService(ExperienceTypeRepository repository) {
        this.repository = repository;
    }

    public ExperienceType create(ExperienceType role) {
        return repository.save(role);
    }

    public Optional<ExperienceType> getById(long id) {
        return repository.findById(id);
    }

    public List<ExperienceType> getAll() {
        return repository.findAll();
    }

    public ExperienceType update(Long id, ExperienceType experience) {
        experience.setId(id);
        return repository.save(experience);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
