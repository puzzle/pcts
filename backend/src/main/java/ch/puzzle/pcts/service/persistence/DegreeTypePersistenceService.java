package ch.puzzle.pcts.service.persistence;
import static ch.puzzle.pcts.Constants.DEGREE_TYPE;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypePersistenceService extends PersistenceBase<DegreeType, DegreeTypeRepository> {
    private final DegreeTypeRepository degreeTypeRepository;

    public DegreeTypePersistenceService(DegreeTypeRepository degreeTypeRepository) {
        super(degreeTypeRepository);
        this.degreeTypeRepository = degreeTypeRepository;
    }

    @Override
    public String entityName() {
        return DEGREE_TYPE;
    }

    public Optional<DegreeType> getByName(String name) {
        return degreeTypeRepository.findByName(name);
    }

}
