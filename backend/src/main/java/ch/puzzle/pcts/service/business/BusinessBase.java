package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.service.persistence.PersistenceService;
import ch.puzzle.pcts.service.validation.ValidationService;
import org.springframework.stereotype.Service;

/**
 * @param <T>
 *            the type of the entity extending {@link Model}
 */
@Service
public abstract class BusinessBase<T extends Model> {
    protected final ValidationService<T> validationService;
    protected final PersistenceService<T> persistenceService;

    protected BusinessBase(ValidationService<T> validationService, PersistenceService<T> persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }

    public T create(T model) {
        validationService.validateOnCreate(model);
        return persistenceService.save(model);
    }

    public T getById(Long id) {
        validationService.validateOnGetById(id);
        return persistenceService.getById(id);
    }

    public T update(Long id, T model) {
        validationService.validateOnUpdate(id, model);
        persistenceService.getById(id);
        model.setId(id);
        return persistenceService.save(model);
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        persistenceService.delete(id);
    }
}
