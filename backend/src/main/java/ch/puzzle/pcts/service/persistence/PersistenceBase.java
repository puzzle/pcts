package ch.puzzle.pcts.service.persistence;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <R>
 *            the Repository of the entity
 */
@Service
public abstract class PersistenceBase<T extends Model, R extends JpaRepository<T, Long>>
        implements
            PersistenceService<T> {

    private final R repository;

    protected PersistenceBase(R repository) {
        this.repository = repository;
    }

    public T getById(Long id) {
        return repository.findById(id).orElseThrow(() -> throwNotFoundError(id.toString()));
    }

    public T getReferenceById(Long id) {
        try {
            return repository.getReferenceById(id);
        } catch (InternalError _) {
            throw throwNotFoundError(id.toString());
        }
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

    public abstract String entityName();

    public PCTSException throwNotFoundError(String attributeValue) {
        Map<FieldKey, String> attributes = Map
                .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, attributeValue);

        GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

        return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
    }
}
