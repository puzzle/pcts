package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.model.error.ErrorKey;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

abstract class ValidationBaseServiceTest<T extends Model, S extends ValidationBase<T>> {

    S service;

    abstract T getModel();

    abstract S getService();

    // The method source 'invalidModelProvider' needs to be overridden by subclasses
    // at all times.
    // If it is not overridden, the "PreconditionViolationException" will be
    // thrown."
    static Stream<Arguments> invalidModelProvider() {
        return Stream.empty();
    }

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("invalidModelProvider")
    @DisplayName("Should throw validation exception for invalid model configurations")
    void validateInvalidModel(T model, String expectedMessage) {
        var validationService = getService();
        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validate(model));
        assertEquals(expectedMessage, exception.getReason());
    }

    @BeforeEach
    void setUp() {
        service = getService();
    }

    @DisplayName("Should be successful validateOnGet() when Id is valid")
    @Test
    void validateOnGetByIdShouldBeSuccessfulWhenIdIsValid() {
        Long id = 1L;
        assertDoesNotThrow(() -> service.validateOnGetById(id));
    }

    @DisplayName("Should throw exception validateOnGet() when Id is null")
    @Test
    void validateOnGetByIdShouldThrowExceptionWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnGetById(id));

        assertEquals("Id must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should be successful when validateOnCreate() model is Valid")
    @Test
    void validateOnCreateShouldBeSuccessfulWhenModelIsValid() {
        T model = getModel();

        assertDoesNotThrow(() -> service.validateOnCreate(model));
    }

    @DisplayName("Should throw exception validateOnCreate() when Id is not null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        T model = getModel();
        model.setId(1L);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(model));

        assertEquals("Id must be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception validateOnCreate() when model is null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(null));

        assertEquals("Model must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should be successful validateOnUpdate() when model is Valid")
    @Test
    void validateOnUpdateShouldBeSuccessfulWhenIdAndModelIsValid() {
        Long id = 1L;
        T model = getModel();
        model.setId(1L);

        assertDoesNotThrow(() -> service.validateOnUpdate(id, model));
    }

    @DisplayName("Should throw exception validateOnUpdate() when id is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        Long id = null;
        T model = getModel();

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, model));

        assertEquals("Id must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception validateOnUpdate() when model is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, null));

        assertEquals("Model must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception validateOnUpdate() when id has changed")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        Long id = 1L;
        T model = getModel();
        model.setId(2L);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, model));

        assertEquals("The queried id must match the id in the model.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should be successful validateOnDelete() when Id is valid")
    @Test
    void validateOnDeleteShouldBeSuccessfulWhenIdIsValid() {
        Long id = 1L;
        assertDoesNotThrow(() -> service.validateOnDelete(id));
    }

    @DisplayName("Should throw exception validateOnDelete() when Id is not null")
    @Test
    void validateOnDeleteShouldThrowExceptionWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnDelete(id));

        assertEquals("Id must not be null.", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}
