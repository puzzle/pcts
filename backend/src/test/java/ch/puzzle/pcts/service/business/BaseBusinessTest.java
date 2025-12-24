package ch.puzzle.pcts.service.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.service.persistence.PersistenceBase;
import ch.puzzle.pcts.service.validation.ValidationBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

abstract class BaseBusinessTest<T extends Model, P extends PersistenceBase<T, ?>, V extends ValidationBase<T>, S extends BusinessBase<T>> {

    private static final Long ID = 1L;

    private T model;
    private P persistenceService;
    private V validationService;
    private S businessService;

    abstract T getModel();
    abstract P getPersistenceService();
    abstract V getValidationService();
    abstract S getBusinessService();

    @BeforeEach
    void setUp() {
        model = getModel();
        persistenceService = getPersistenceService();
        validationService = getValidationService();
        businessService = getBusinessService();
    }

    @DisplayName("Should get model by id")
    @Test
    void shouldGetById() {
        when(persistenceService.getById(ID)).thenReturn(model);

        T result = businessService.getById(ID);

        assertEquals(model, result);

        verify(validationService).validateOnGetById(ID);
        verify(persistenceService).getById(ID);
    }

    @DisplayName("Should create new model")
    @Test
    void shouldCreate() {
        when(persistenceService.save(model)).thenReturn(model);

        T result = businessService.create(model);

        assertEquals(model, result);

        verify(validationService).validateOnCreate(model);
        verify(persistenceService).save(model);
    }

    @DisplayName("Should update model")
    @Test
    void shouldUpdate() {
        when(persistenceService.getById(ID)).thenReturn(model);
        when(persistenceService.save(model)).thenReturn(model);

        T result = businessService.update(ID, model);

        assertEquals(model, result);

        verify(model).setId(ID);
        verify(validationService).validateOnUpdate(ID, model);
        verify(persistenceService).save(model);
    }

    @DisplayName("Should delete model")
    @Test
    void shouldDelete() {
        businessService.delete(ID);

        verify(validationService).validateOnDelete(ID);
        verify(persistenceService).delete(ID);
    }
}
