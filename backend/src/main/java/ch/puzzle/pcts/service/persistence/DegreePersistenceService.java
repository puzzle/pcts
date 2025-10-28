package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.repository.DegreeRepository;
import org.springframework.stereotype.Service;

@Service
public class DegreePersistenceService extends PersistenceBase<Degree, DegreeRepository> {
    public DegreePersistenceService(DegreeRepository repository) {
        super(repository);
    }
}
