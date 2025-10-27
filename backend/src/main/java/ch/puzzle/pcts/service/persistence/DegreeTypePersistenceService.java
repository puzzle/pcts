package ch.puzzle.pcts.service.persistence;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DegreeTypePersistenceService extends PersistenceBase<DegreeType, DegreeTypeRepository> {
    private final DegreeTypeRepository repository;

    public DegreeTypePersistenceService(DegreeTypeRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public Optional<DegreeType> getByName(String name) {
        return repository.findByName(name);
    }

}
