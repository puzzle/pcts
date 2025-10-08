package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.example.Example;
import ch.puzzle.pcts.repository.ExampleRepository;

class ExamplePersistenceServiceIT extends PersistenceBaseIT<Example, ExampleRepository, ExamplePersistenceService> {

    ExamplePersistenceServiceIT(ExamplePersistenceService service) {
        super(service);
    }

    @Override
    Example getCreateEntity() {
        return new Example(null, "Example 3");
    }

    @Override
    Example getUpdateEntity() {
        return new Example(null, "Updated example");
    }

    @Override
    Long getId(Example example) {
        return example.getId();
    }

    @Override
    void setId(Example example, Long id) {
        example.setId(id);
    }
}