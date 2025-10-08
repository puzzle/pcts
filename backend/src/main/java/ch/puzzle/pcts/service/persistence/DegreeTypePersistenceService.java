package ch.puzzle.pcts.service.persistence;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypePersistenceService extends PersistenceBase<DegreeType, Long> {
    private final DegreeTypeRepository repository;

    public DegreeTypePersistenceService(DegreeTypeRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
