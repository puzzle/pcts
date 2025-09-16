package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.repository.DegreeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DegreeTypePersistenceService {
    private final DegreeTypeRepository repository;

    @Autowired
    public DegreeTypePersistenceService(DegreeTypeRepository repository) {
        this.repository = repository;
    }
}
