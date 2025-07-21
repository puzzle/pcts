package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.dto.example.CreateExampleDto;
import ch.puzzle.pcts.model.example.Example;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// This should really be something like this:
// @Repository
// public interface ExampleRepository extends CrudRepository<> { ... }
@Component
public class ExampleRepository {
    private List<Example> database = new ArrayList<>();

    public ExampleRepository(){
        this.database.add(new Example(1, "Some example"));
        this.database.add(new Example(2, "Another one"));
    }

    public List<Example> getAll(){
        return database;
    }

    public Example add(CreateExampleDto example){
        database.add(new Example(database.size() + 1, example.text()));
        return database.getLast();
    }

    public Example getById(long id){
        return new Example(id, "This is example #" + id);
    }
}
