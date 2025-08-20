package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.dto.example.CreateExampleDto;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.repository.ExampleRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExamplePersistenceService {
    private final ExampleRepository repository;

    @Autowired
    public ExamplePersistenceService(ExampleRepository repository) {
        this.repository = repository;
    }

    public Example create(CreateExampleDto dto) {
        return repository.add(dto);
    }

    public Example getById(long id) {
        return repository.getById(id);
    }

    public List<Example> getAll() {
        return repository.getAll();
    }
}
