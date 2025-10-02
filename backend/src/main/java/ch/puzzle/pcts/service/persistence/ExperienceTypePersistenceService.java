package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypePersistenceService {

    private final ExperienceTypeRepository repository;

    public ExperienceTypePersistenceService(ExperienceTypeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ExperienceType create(ExperienceType experienceType) {
        experienceType.setName(experienceType.getName().trim());
        return repository.save(experienceType);
    }

    public Optional<ExperienceType> getById(long id) {
        return repository.findById(id);
    }

    public List<ExperienceType> getAll() {
        return repository.findAll();
    }

    @Transactional
    public ExperienceType update(Long id, ExperienceType experienceType) {
        experienceType.setName(experienceType.getName().trim());
        experienceType.setId(id);
        return repository.save(experienceType);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
