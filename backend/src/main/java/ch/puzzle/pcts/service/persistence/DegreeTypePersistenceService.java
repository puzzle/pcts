package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypePersistenceService {
    private final DegreeTypeRepository repository;

    public DegreeTypePersistenceService(DegreeTypeRepository repository) {
        this.repository = repository;
    }

    public List<DegreeType> getAll() {
        return repository.findAll();
    }

    @Transactional
    public DegreeType create(DegreeType degreeType) {
        return repository.save(degreeType);
    }

    public Optional<DegreeType> getById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public DegreeType update(Long id, DegreeType degreeType) {
        degreeType.setId(id);
        return repository.save(degreeType);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
