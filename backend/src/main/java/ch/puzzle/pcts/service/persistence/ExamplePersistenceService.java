package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.repository.ExampleRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExamplePersistenceService {
    private final ExampleRepository repository;

    public ExamplePersistenceService(ExampleRepository repository) {
        this.repository = repository;
    }

    public Example create(Example example) {
        return repository.save(example);
    }

    public Example getById(long id) {
        return repository.getById(id);
    }

    public List<Example> getAll() {
        return repository.findAll();
    }
}
