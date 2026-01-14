package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.util.TestData.GENERIC_1_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.service.persistence.PersistenceBase;
import ch.puzzle.pcts.service.validation.ValidationBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

abstract class BaseBusinessTest<T extends Model, P extends PersistenceBase<T, ?>, V extends ValidationBase<T>, S extends BusinessBase<T>> {

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
        when(persistenceService.getById(GENERIC_1_ID)).thenReturn(model);

        T result = businessService.getById(GENERIC_1_ID);

        assertEquals(model, result);

        verify(validationService).validateOnGetById(GENERIC_1_ID);
        verify(persistenceService).getById(GENERIC_1_ID);
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
        when(persistenceService.getById(GENERIC_1_ID)).thenReturn(model);
        when(persistenceService.save(model)).thenReturn(model);

        T result = businessService.update(GENERIC_1_ID, model);

        assertEquals(model, result);

        verify(model).setId(GENERIC_1_ID);
        verify(validationService).validateOnUpdate(GENERIC_1_ID, model);
        verify(persistenceService).save(model);
    }

    @DisplayName("Should delete model")
    @Test
    void shouldDelete() {
        businessService.delete(GENERIC_1_ID);

        verify(validationService).validateOnDelete(GENERIC_1_ID);
        verify(persistenceService).delete(GENERIC_1_ID);
    }
}
