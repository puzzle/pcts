package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import ch.puzzle.pcts.service.validation.ExampleValidationService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {
    private final ExampleValidationService validationService;
    private final ExamplePersistenceService persistenceService;

    public ExampleService(ExampleValidationService validationService, ExamplePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public Example getById(long id) {
        return persistenceService.getById(id);
    }

    public List<Example> getAll() {
        return persistenceService.getAll();
    }

    @Transactional
    public Example create(Example example) {
        validationService.validateOnCreate(example);
        return persistenceService.create(example);
    }
}
