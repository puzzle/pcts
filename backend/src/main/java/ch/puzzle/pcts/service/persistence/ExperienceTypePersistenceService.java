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

    public ExperienceType create(ExperienceType experienceType) {
        experienceType.roundPoints();
        return repository.save(experienceType);
    }

    public Optional<ExperienceType> getById(long id) {
        return repository.findById(id);
    }

    public List<ExperienceType> getAll() {
        return repository.findAll();
    }

    public ExperienceType update(Long id, ExperienceType experienceType) {
        experienceType.setId(id);
        experienceType.roundPoints();
        return repository.save(experienceType);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
