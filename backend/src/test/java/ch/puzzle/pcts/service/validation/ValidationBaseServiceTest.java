package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.util.TestData.GENERIC_1_ID;
import static ch.puzzle.pcts.util.TestData.GENERIC_2_ID;
import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

abstract class ValidationBaseServiceTest<T extends Model, S extends ValidationBase<T>> {

    S service;

    abstract T getValidModel();

    abstract S getService();

    @BeforeEach
    void setUp() {
        service = getService();
    }

    /**
     * Provides invalid model configurations for parameterized tests.
     * <p>
     * Subclasses must override this method to supply test arguments. If it is not
     * overridden, a {@code PreconditionViolationException} will be thrown.
     * <p>
     * If you don't have any validations on the model, you may return an empty
     * stream. Make sure to add a comment in the test class explaining why.
     *
     * @return a stream of arguments containing invalid model configurations
     */
    static Stream<Arguments> invalidModelProvider() {
        return Stream.empty();
    }

    @ParameterizedTest(name = "{index}: {1}") // Displays test index and expected message in the test name
    @MethodSource("invalidModelProvider")
    @DisplayName("Should throw validation exception for invalid model configurations")
    void validateInvalidModel(T model, List<Map<FieldKey, String>> expectedAttributes) {
        S validationService = getService();
        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validate(model));
        assertEquals(expectedAttributes, exception.getErrorAttributes());
    }

    @DisplayName("Should be successful validateOnGet() when Id is valid")
    @Test
    void validateOnGetByIdShouldBeSuccessfulWhenIdIsValid() {
        assertDoesNotThrow(() -> service.validateOnGetById(GENERIC_1_ID));
    }

    @DisplayName("Should throw exception validateOnGet() when Id is null")
    @Test
    void validateOnGetByIdShouldThrowExceptionWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnGetById(id));

        assertEquals(List.of(ErrorKey.VALIDATION), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id")), exception.getErrorAttributes());
    }

    @DisplayName("Should be successful when validateOnCreate() model is Valid")
    @Test
    void validateOnCreateShouldBeSuccessfulWhenModelIsValid() {
        T model = getValidModel();

        assertDoesNotThrow(() -> service.validateOnCreate(model));
    }

    @DisplayName("Should throw exception validateOnCreate() when Id is not null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        T model = getValidModel();
        model.setId(GENERIC_1_ID);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(model));

        assertEquals(List.of(ErrorKey.VALIDATION), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id")), exception.getErrorAttributes());
    }

    @DisplayName("Should throw exception validateOnCreate() when model is null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(null));

        assertEquals(List.of(ErrorKey.VALIDATION), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id")), exception.getErrorAttributes());
    }

    @DisplayName("Should be successful validateOnUpdate() when model is Valid")
    @Test
    void validateOnUpdateShouldBeSuccessfulWhenIdAndModelIsValid() {
        T model = getValidModel();
        model.setId(GENERIC_1_ID);

        assertDoesNotThrow(() -> service.validateOnUpdate(GENERIC_1_ID, model));
    }

    @DisplayName("Should throw exception validateOnUpdate() when id is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        Long id = null;
        T model = getValidModel();

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, model));

        assertEquals(List.of(ErrorKey.VALIDATION), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id")), exception.getErrorAttributes());
    }

    @DisplayName("Should throw exception validateOnUpdate() when model is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(GENERIC_1_ID, null));

        assertEquals(List.of(ErrorKey.VALIDATION), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id")), exception.getErrorAttributes());
    }

    @DisplayName("Should throw exception validateOnUpdate() when id has changed")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        T model = getValidModel();
        model.setId(GENERIC_2_ID);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service.validateOnUpdate(GENERIC_1_ID, model));

        assertEquals(List.of(ErrorKey.VALIDATION), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id")), exception.getErrorAttributes());
    }

    @DisplayName("Should be successful validateOnDelete() when Id is valid")
    @Test
    void validateOnDeleteShouldBeSuccessfulWhenIdIsValid() {
        assertDoesNotThrow(() -> service.validateOnDelete(GENERIC_1_ID));
    }

    @DisplayName("Should throw exception validateOnDelete() when Id is not null")
    @Test
    void validateOnDeleteShouldThrowExceptionWhenIdIsNull() {
        Long id = null;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnDelete(id));

        assertEquals(List.of(ErrorKey.VALIDATION), exception.getErrorKeys());
        assertEquals(List.of(Map.of(FieldKey.FIELD, "id")), exception.getErrorAttributes());
    }

    @DisplayName("Should throw exception when the date passed first is after the second date")
    @Test
    void validateDateIsBeforeShouldThrowExceptionWhenDateIsAfter() {
        LocalDate pastDate = LocalDate.of(2020, 1, 2);
        LocalDate currentDate = LocalDate.of(2026, 1, 15);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> service
                                                       .validateDateIsBefore("ENTITY",
                                                                             "EarlyDate",
                                                                             currentDate,
                                                                             "LateDate",
                                                                             pastDate));

        assertEquals(List.of(ErrorKey.ATTRIBUTE_NOT_BEFORE), exception.getErrorKeys());
        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "EarlyDate",
                            FieldKey.MAX,
                            "2020-01-02",
                            FieldKey.CONDITION_FIELD,
                            "LateDate",
                            FieldKey.ENTITY,
                            "ENTITY",
                            FieldKey.IS,
                            "2026-01-15")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Accepts early date before or equal to late date, including null values")
    @ParameterizedTest(name = "ED: {index}: {0}" + " - " + "LD: {index}: {1}")
    @MethodSource("dateProvider")
    void validateDateIsBeforeShouldNotThrowExceptionWhenDateIsValid(LocalDate earlyDate, LocalDate lateDate) {
        assertDoesNotThrow(() -> service.validateDateIsBefore("ENTITY", "EarlyDate", earlyDate, "LateDate", lateDate));
    }

    static Stream<Arguments> dateProvider() {
        return Stream
                .of(Arguments.of(LocalDate.of(2021, 5, 23), LocalDate.of(2024, 8, 4)),
                    Arguments.of(LocalDate.of(1999, 3, 27), LocalDate.of(1999, 3, 27)),
                    Arguments.of(LocalDate.now(), null),
                    Arguments.of(null, LocalDate.now()));
    }
}
