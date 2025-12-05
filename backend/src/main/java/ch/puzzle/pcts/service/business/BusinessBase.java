package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.service.persistence.PersistenceService;
import ch.puzzle.pcts.service.validation.ValidationService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
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
        return persistenceService.getById(id).orElseThrow(() -> {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            return new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        });
    }

    public T update(Long id, T model) {
        if (this.getById(id) != null) {
            validationService.validateOnUpdate(id, model);
            model.setId(id);
            return persistenceService.save(model);
        } else {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));

        }
    }

    public void delete(Long id) {
        validationService.validateOnDelete(id);
        if (persistenceService.getById(id).isPresent()) {
            persistenceService.delete(id);
        } else {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, entityName(), FieldKey.FIELD, "id", FieldKey.IS, id.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.NOT_FOUND, attributes);

            throw new PCTSException(HttpStatus.NOT_FOUND, List.of(error));
        }
    }

    protected abstract String entityName();
}
