package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.dto.example.ExampleDto;
import ch.puzzle.pcts.mapper.ExampleMapper;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import ch.puzzle.pcts.service.validation.ExampleValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExampleService {
    private final ExampleValidationService validationService;
    private final ExamplePersistenceService persistenceService;
    private final ExampleMapper exampleMapper;

    public ExampleService(ExampleValidationService validationService, ExamplePersistenceService persistenceService, ExampleMapper exampleMapper) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
        this.exampleMapper = exampleMapper;
    }

    public Example getById(long id) {
        return persistenceService.getById(id);
    }

    public List<Example> getAll() {
        return persistenceService.getAll();
    }

    public Example create(ExampleDto dto) {
        validationService.validateOnCreate(dto);
        return persistenceService.create(exampleMapper.fromDto(dto));
    }
}
