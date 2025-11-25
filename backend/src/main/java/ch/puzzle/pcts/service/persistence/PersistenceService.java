package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.model.Model;
import java.util.List;
import java.util.Optional;

public interface PersistenceService<T extends Model> {
    Optional<T> getById(Long id);

    List<T> getAll();

    T save(T model);

    void delete(Long id);
}
