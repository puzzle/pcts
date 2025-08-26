package ch.puzzle.pcts.repository;

import ch.puzzle.pcts.model.example.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {
    Example getById(long id);
}
