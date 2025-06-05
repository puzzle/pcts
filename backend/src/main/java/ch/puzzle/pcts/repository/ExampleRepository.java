package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.Example;
import org.springframework.stereotype.Component;

import java.util.List;

// This should really be something like this:
// @Repository
// public interface ExampleRepository extends CrudRepository<> { ... }
@Component
public class ExampleRepository {

    public List<Example> getAll(){
        return List.of(
                new Example(1, "Some example"),
                new Example(2, "Another one")
        );
    }

    public Example getById(long id){
        return new Example(id, "This is example #" + id);
    }
}
