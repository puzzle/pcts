package ch.puzzle.pcts.service.persistence;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <I>
 *            the Identifier or primary key of the entity
 */
public abstract class PersistenceBase<T, I> {

    private final JpaRepository<T, I> repository;

    protected PersistenceBase(JpaRepository<T, I> repository) {
        this.repository = repository;
    }

    public Optional<T> getById(I id) {
        return repository.findById(id);
    }

    public List<T> getAll() {
        return repository.findAll();
    }

    @Transactional
    public T save(T model) {
        return repository.save(model);
    }

    public void delete(I id) {
        repository.deleteById(id);
    }
}
