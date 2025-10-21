package ch.puzzle.pcts.service.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <R>
 *            the Repository of the entity
 */
public abstract class PersistenceBase<T, R extends JpaRepository<T, Long>> {

    private final R repository;

    protected PersistenceBase(R repository) {
        this.repository = repository;
    }

    public Optional<T> getById(Long id) {
        return repository.findById(id);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    public T save(T model) {
        return repository.save(model);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
