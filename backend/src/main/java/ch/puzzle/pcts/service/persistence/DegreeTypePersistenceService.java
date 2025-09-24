package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.degree_type.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DegreeTypePersistenceService {
    private final DegreeTypeRepository repository;

    private final EntityManager entityManager;

    @Autowired
    public DegreeTypePersistenceService(DegreeTypeRepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    public List<DegreeType> getAll() {
        return repository.findAll();
    }

    @Transactional
    public DegreeType create(DegreeType degreeType) {
        DegreeType createdDegreeType = repository.saveAndFlush(degreeType);
        entityManager.refresh(createdDegreeType);
        return createdDegreeType;
    }

    public Optional<DegreeType> getById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public DegreeType update(Long id, DegreeType degreeType) {
        degreeType.setId(id);
        DegreeType updatedDegreeType = repository.saveAndFlush(degreeType);
        entityManager.refresh(updatedDegreeType);
        return updatedDegreeType;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
