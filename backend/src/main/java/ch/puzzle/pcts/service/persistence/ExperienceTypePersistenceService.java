package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.experienceType.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExperienceTypePersistenceService {
    private final EntityManager entityManager;

    private final ExperienceTypeRepository repository;

    @Autowired
    public ExperienceTypePersistenceService(EntityManager entityManager, ExperienceTypeRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    public ExperienceType create(ExperienceType experienceType) {
        ExperienceType createdExperienceType = repository.saveAndFlush(experienceType);
        entityManager.refresh(createdExperienceType);
        return createdExperienceType;
    }

    public Optional<ExperienceType> getById(long id) {
        return repository.findById(id);
    }

    public List<ExperienceType> getAll() {
        return repository.findAll();
    }

    public ExperienceType update(Long id, ExperienceType experienceType) {
        experienceType.setId(id);
        ExperienceType updatedExperienceType = repository.saveAndFlush(experienceType);
        entityManager.refresh(updatedExperienceType);
        return updatedExperienceType;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
