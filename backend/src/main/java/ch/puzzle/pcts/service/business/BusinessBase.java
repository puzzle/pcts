package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.service.persistence.PersistenceBase;
import ch.puzzle.pcts.service.validation.ValidationBase;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @param <T>
 *            the type of the entity extending {@link Model}
 * @param <R>
 *            the type of the JPA repository for the entity
 * @param <V>
 *            the validation service for the entity
 * @param <P>
 *            the persistence service for the entity
 */
@Service
public abstract class BusinessBase<T extends Model, R extends JpaRepository<T, Long>, V extends ValidationBase<T>, P extends PersistenceBase<T, R>> {
    protected final V validationService;
    protected final P persistenceService;

    protected BusinessBase(V validationService, P persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public T create(T model) {
        validationService.validateOnCreate(model);
        return persistenceService.save(model);
    }

    public T getById(Long id) {
        validationService.validateOnGetById(id);
        return persistenceService
                .getById(id)
                .orElseThrow(() -> new PCTSException(HttpStatus.NOT_FOUND,
                                                     ErrorKey.NOT_FOUND,
                                                     Map
                                                             .of(FieldKey.ENTITY,
                                                                 entityName(),
                                                                 FieldKey.FIELD,
                                                                 "id",
                                                                 FieldKey.IS,
                                                                 id.toString())));
    }

    public T update(Long id, T model) {
        if (persistenceService.getById(id).isPresent()) {
            validationService.validateOnUpdate(id, model);
            model.setId(id);
            return persistenceService.save(model);
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    ErrorKey.NOT_FOUND,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                entityName(),
                                                FieldKey.FIELD,
                                                "id",
                                                FieldKey.IS,
                                                id.toString()));
        }
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        if (persistenceService.getById(id).isPresent()) {
            persistenceService.delete(id);
        } else {
            throw new PCTSException(HttpStatus.NOT_FOUND,
                                    ErrorKey.NOT_FOUND,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                entityName(),
                                                FieldKey.FIELD,
                                                "id",
                                                FieldKey.IS,
                                                id.toString()));
        }
    }

    protected abstract String entityName();
}
