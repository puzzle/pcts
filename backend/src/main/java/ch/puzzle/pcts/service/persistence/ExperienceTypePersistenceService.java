package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypePersistenceService {

    private final ExperienceTypeRepository repository;

    public ExperienceTypePersistenceService(ExperienceTypeRepository repository) {
        this.repository = repository;
    }

    public ExperienceType create(ExperienceType experienceType) {
        return repository.save(experienceType);
    }

    public Optional<ExperienceType> getById(Long id) {
        return repository.findById(id);
    }

    public List<ExperienceType> getAll() {
        return repository.findAll();
    }

    public ExperienceType update(Long id, ExperienceType experienceType) {
        experienceType.setId(id);
        return repository.save(experienceType);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
