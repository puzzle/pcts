package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree_type.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DegreeTypeValidationServiceTest {
    private AutoCloseable closeable;

    @Mock
    private DegreeTypePersistenceService persistenceService;

    @InjectMocks
    private DegreeTypeValidationService validationService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @DisplayName("Should be successful on validateOnGetById() when id valid")
    @Test
    void shouldBeSuccessfulOnValidateOnGetByIdWhenIdIsValid() {
        long id = 1L;

        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));
        assertDoesNotThrow(() -> validationService.validateOnGetById(id));
    }

    @DisplayName("Should throw exception on validateOnGetById() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenIdIsInvalid() {
        long id = -1;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnGetById(id));

        assertEquals("Degree type with degreeTypeId: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when degree type is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenDegreeTypeIsValid() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("New Degree type");
        degreeType.setHighlyRelevantPoints(new BigDecimal("1.0"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("2.0"));
        degreeType.setLittleRelevantPoints(new BigDecimal("3.0"));

        assertDoesNotThrow(() -> validationService.validateOnCreate(degreeType));
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        DegreeType degreeType = new DegreeType();
        degreeType.setDegreeTypeId(123L);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(degreeType));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameIsNull() {
        DegreeType degreeType = new DegreeType();

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(degreeType));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is blank")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameBlank() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("");

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(degreeType));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_NAME_IS_EMPTY, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when relevant points are null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenRelevantPointsAreNull() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(degreeType));

        assertEquals("relevant points must not be null", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_ARE_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when relevant points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenRelevantPointsAreNegative() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");
        degreeType.setHighlyRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLittleRelevantPoints(new BigDecimal("-1.0"));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(degreeType));

        assertEquals("relevant points must not be negative", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_ARE_NEGATIVE, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when relevant points have more than 2 decimal places")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenRelevantPointsHaveMoreThanTwoDecimalPlaces() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");
        degreeType.setHighlyRelevantPoints(new BigDecimal("1.123"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("1.123"));
        degreeType.setLittleRelevantPoints(new BigDecimal("1.123"));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(degreeType));

        assertEquals("relevant points must not have more than 2 decimal places", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_HAVE_TOO_MANY_DECIMAL_PLACES, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnUpdate() when everything is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnUpdateWhenIdIsValid() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");
        degreeType.setHighlyRelevantPoints(new BigDecimal("1.25"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("1.25"));
        degreeType.setLittleRelevantPoints(new BigDecimal("1.25"));
        long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));

        assertDoesNotThrow(() -> validationService.validateOnUpdate(id, degreeType));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateIdWhenIdIsInvalid() {
        DegreeType degreeType = new DegreeType();
        long id = -1;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, degreeType));

        assertEquals("Degree type with degreeTypeId: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNotNull() {
        DegreeType degreeType = new DegreeType();
        degreeType.setDegreeTypeId(123L);
        long id = 1;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, degreeType));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameIsNull() {
        DegreeType degreeType = new DegreeType();
        long id = 1;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, degreeType));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is blank")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameBlank() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("");
        long id = 1;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, degreeType));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_NAME_IS_EMPTY, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when relevant points are null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenRelevantPointsAreNull() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, degreeType));

        assertEquals("relevant points must not be null", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_ARE_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when relevant points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenRelevantPointsAreNegative() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");
        degreeType.setHighlyRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLittleRelevantPoints(new BigDecimal("-1.0"));
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, degreeType));

        assertEquals("relevant points must not be negative", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_ARE_NEGATIVE, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when relevant points have more than two decimal places")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenRelevantPointsHaveMoreThanTwoDecimalPlaces() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");
        degreeType.setHighlyRelevantPoints(new BigDecimal("1.123"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("1.123"));
        degreeType.setLittleRelevantPoints(new BigDecimal("1.123"));
        Long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, degreeType));

        assertEquals("relevant points must not have more than 2 decimal places", exception.getReason());
        assertEquals(ErrorKey.DEGREE_TYPE_RELEVANT_POINTS_HAVE_TOO_MANY_DECIMAL_PLACES, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnDelete() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnDeleteWhenIdIsValid() {
        long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new DegreeType()));

        assertDoesNotThrow(() -> validationService.validateOnDelete(id));
    }

    @DisplayName("Should throw exception on validateOnDelete() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnDeleteIdWhenIdIsInvalid() {
        long id = -1;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnDelete(id));

        assertEquals("Degree type with degreeTypeId: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }
}