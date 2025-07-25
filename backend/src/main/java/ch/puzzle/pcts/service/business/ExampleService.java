package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.dto.example.CreateExampleDto;
import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import ch.puzzle.pcts.service.validation.ExampleValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExampleService {
    private final ExampleValidationService validationService;
    private final ExamplePersistenceService persistenceService;

    public ExampleService(ExampleValidationService validationService, ExamplePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public Example getById(long id){
        return persistenceService.getById(id);
    }

    public List<Example> getAll(){
        return persistenceService.getAll();
    }

    public Example create(CreateExampleDto dto){
        validationService.validateOnCreate(dto);
        return persistenceService.create(dto);
    }
}
