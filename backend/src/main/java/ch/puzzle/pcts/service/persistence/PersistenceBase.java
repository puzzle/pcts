package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;

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

    public T getById(I id) throws PCTSException {
        return repository
                .findById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     getModelName() + " with id: " + id + " does not exist.",
                                                     ErrorKey.NOT_FOUND));
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

    public abstract String getModelName();
}
