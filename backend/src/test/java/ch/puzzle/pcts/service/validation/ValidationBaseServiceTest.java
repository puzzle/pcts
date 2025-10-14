package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.error.ErrorKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

abstract class ValidationBaseServiceTest<T extends Model, S extends ValidationBase<T>> {

    protected S service;

    abstract T getModel();

    abstract void validate();

    abstract S getService();

    @BeforeEach
    void setUp() {
        service = getService();
    }

    @DisplayName("Should be successful when Id is valid")
    @Test
    void validateOnGetShouldBeSuccessfulWhenIdIsValid() {
        Long id = 1L;
        assertDoesNotThrow(() -> service.validateOnGet(id));
    }

    @DisplayName("Should throw exception when Id is null")
    @Test
    void validateOnGetShouldThrowExceptionWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnGet(id));

        assertEquals("Id must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should be successful when model is Valid")
    @Test
    void validateOnCreateShouldBeSuccessfulWhenModelIsValid() {
        T model = getModel();

        assertDoesNotThrow(() -> service.validateOnCreate(model));
    }

    @DisplayName("Should throw exception when Id is not null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        T model = getModel();
        model.setId(1L);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(model));

        assertEquals("Id must be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception when model is null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(null));

        assertEquals("Model must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should be successful when model is Valid")
    @Test
    void validateOnUpdateShouldBeSuccessfulWhenIdAndModelIsValid() {
        Long id = 1L;
        T model = getModel();
        model.setId(1L);

        assertDoesNotThrow(() -> service.validateOnUpdate(id, model));
    }

    @DisplayName("Should throw exception when id is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        Long id = null;
        T model = getModel();

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, model));

        assertEquals("Id must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception when model is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, null));

        assertEquals("Model must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception when id has changed")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        Long id = 1L;
        T model = getModel();
        model.setId(2L);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, model));

        assertEquals("The queried id must match the id in the model.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should be successful when Id is valid")
    @Test
    void validateOnDeleteShouldBeSuccessfulWhenIdIsValid() {
        Long id = 1L;
        assertDoesNotThrow(() -> service.validateOnDelete(id));
    }

    @DisplayName("Should throw exception when Id is not null")
    @Test
    void validateOnDeleteShouldThrowExceptionWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnDelete(id));

        assertEquals("Id must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}
