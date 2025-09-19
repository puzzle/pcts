package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.dto.degreeType.DegreeTypeNameDto;
import ch.puzzle.pcts.model.degreeType.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DegreeTypePersistenceService {
    private final DegreeTypeRepository repository;

    @Autowired
    public DegreeTypePersistenceService(DegreeTypeRepository repository) {
        this.repository = repository;
    }

    public List<DegreeType> getAll() {
        return repository.findAll();
    }

    public DegreeType create(DegreeType degreeType) {
        return repository.save(degreeType);
    }

    public Optional<DegreeType> getById(Long id) {
        return repository.findById(id);
    }

    public DegreeType update(Long id, DegreeType degreeType) {
        degreeType.setId(id);
        return repository.save(degreeType);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<DegreeTypeNameDto> getAllNames() {
        return repository.findAllNames();
    }
}
