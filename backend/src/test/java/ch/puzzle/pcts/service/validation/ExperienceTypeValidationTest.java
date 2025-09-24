package ch.puzzle.pcts.service.validation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experienceType.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ExperienceTypeValidationTest {
    private AutoCloseable closeable;

    @Mock
    private ExperienceTypePersistenceService persistenceService;

    @InjectMocks
    private ExperienceTypeValidationService validationService;

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

        when(persistenceService.getById(id)).thenReturn(Optional.of(new ExperienceType()));
        assertDoesNotThrow(() -> validationService.validateOnGetById(id));
    }

    @DisplayName("Should throw exception on validateOnGetById() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnGetByIdWhenIdIsInvalid() {
        long id = -1;

        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnGetById(id));

        assertEquals("ExperienceType with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnCreate() when experienceType is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenExperienceTypeIsValid() {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName("New experienceType");
        experienceType.setHighlyRelevantPoints(BigDecimal.valueOf(4));
        experienceType.setLimitedRelevantPoints(BigDecimal.valueOf(2));
        experienceType.setLittleRelevantPoints(BigDecimal.valueOf(1));

        assertDoesNotThrow(() -> validationService.validateOnCreate(experienceType));
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName("ExperienceType");
        experienceType.setId(123L);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(experienceType));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameIsNull() {
        ExperienceType experienceType = new ExperienceType();

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(experienceType));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.EXPERIENCE_TYPE_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name is blank")
    @ParameterizedTest
    @ValueSource(strings = { "", "   " })
    void shouldThrowExceptionOnValidateOnCreateWhenNameBlank(String name) {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName(name);

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(experienceType));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.EXPERIENCE_TYPE_NAME_IS_EMPTY, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNull() {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName("ExperienceType");
        experienceType.setHighlyRelevantPoints(BigDecimal.valueOf(4));
        experienceType.setLimitedRelevantPoints(null);
        experienceType.setLittleRelevantPoints(BigDecimal.valueOf(9));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(experienceType));

        assertEquals("ExperienceType has points with null as value", exception.getReason());
        assertEquals(ErrorKey.EXPERIENCE_TYPE_POINTS_ARE_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenPointsAreNegative() {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName("ExperienceType");
        experienceType.setHighlyRelevantPoints(BigDecimal.valueOf(-1));
        experienceType.setLimitedRelevantPoints(BigDecimal.valueOf(5));
        experienceType.setLittleRelevantPoints(BigDecimal.valueOf(9));

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnCreate(experienceType));

        assertEquals("ExperienceType has negative points", exception.getReason());
        assertEquals(ErrorKey.EXPERIENCE_TYPE_POINTS_ARE_NEGATIVE, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnDelete() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnDeleteWhenIdIsValid() {
        long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new ExperienceType()));

        assertDoesNotThrow(() -> validationService.validateOnDelete(id));
    }

    @DisplayName("Should throw exception on validateOnDelete() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnDeleteIdWhenIdIsInvalid() {
        long id = -1;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class, () -> validationService.validateOnDelete(id));

        assertEquals("ExperienceType with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should be successful on validateOnUpdate() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnUpdateWhenIdIsValid() {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName("ExperienceType");
        experienceType.setHighlyRelevantPoints(BigDecimal.valueOf(4));
        experienceType.setLimitedRelevantPoints(BigDecimal.valueOf(2));
        experienceType.setLittleRelevantPoints(BigDecimal.valueOf(1));

        long id = 1L;
        when(persistenceService.getById(id)).thenReturn(Optional.of(new ExperienceType()));

        assertDoesNotThrow(() -> validationService.validateOnUpdate(id, experienceType));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is invalid")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateIdWhenIdIsInvalid() {
        ExperienceType experienceType = new ExperienceType();
        long id = -1;
        when(persistenceService.getById(id)).thenReturn(Optional.empty());

        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(id, experienceType));

        assertEquals("ExperienceType with id: " + id + " does not exist.", exception.getReason());
        assertEquals(ErrorKey.NOT_FOUND, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNotNull() {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName("ExperienceType");
        experienceType.setId(123L);

        when(persistenceService.getById(1L)).thenReturn(Optional.of(new ExperienceType()));
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(1L, experienceType));

        assertEquals("Id needs to be undefined", exception.getReason());
        assertEquals(ErrorKey.ID_IS_NOT_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameIsNull() {
        ExperienceType experienceType = new ExperienceType();

        when(persistenceService.getById(1L)).thenReturn(Optional.of(new ExperienceType()));
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(1L, experienceType));

        assertEquals("Name must not be null", exception.getReason());
        assertEquals(ErrorKey.EXPERIENCE_TYPE_NAME_IS_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when name is blank")
    @ParameterizedTest
    @ValueSource(strings = { "", "   " })
    void shouldThrowExceptionOnValidateOnUpdateWhenNameBlank(String name) {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName(name);

        when(persistenceService.getById(1L)).thenReturn(Optional.of(new ExperienceType()));
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(1L, experienceType));

        assertEquals("Name must not be empty", exception.getReason());
        assertEquals(ErrorKey.EXPERIENCE_TYPE_NAME_IS_EMPTY, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNull() {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName("ExperienceType");
        experienceType.setHighlyRelevantPoints(BigDecimal.valueOf(4));
        experienceType.setLimitedRelevantPoints(null);
        experienceType.setLittleRelevantPoints(BigDecimal.valueOf(9));

        when(persistenceService.getById(1L)).thenReturn(Optional.of(new ExperienceType()));
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(1L, experienceType));

        assertEquals("ExperienceType has points with null as value", exception.getReason());
        assertEquals(ErrorKey.EXPERIENCE_TYPE_POINTS_ARE_NULL, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenPointsAreNegative() {
        ExperienceType experienceType = new ExperienceType();
        experienceType.setName("ExperienceType");
        experienceType.setHighlyRelevantPoints(BigDecimal.valueOf(-1));
        experienceType.setLimitedRelevantPoints(BigDecimal.valueOf(5));
        experienceType.setLittleRelevantPoints(BigDecimal.valueOf(9));

        when(persistenceService.getById(1L)).thenReturn(Optional.of(new ExperienceType()));
        PCTSException exception = assertThrows(PCTSException.class,
                                               () -> validationService.validateOnUpdate(1L, experienceType));

        assertEquals("ExperienceType has negative points", exception.getReason());
        assertEquals(ErrorKey.EXPERIENCE_TYPE_POINTS_ARE_NEGATIVE, exception.getErrorKey());
    }
}
