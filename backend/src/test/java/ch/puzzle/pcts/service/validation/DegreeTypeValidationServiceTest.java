package ch.puzzle.pcts.service.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeTypeValidationServiceTest extends ValidationBaseServiceTest<DegreeType, DegreeTypeValidationService> {

    @InjectMocks
    DegreeTypeValidationService service;

    @Mock
    private DegreeTypePersistenceService persistenceService;

    @Override
    DegreeType getModel() {
        return new DegreeType(null,
                              "Degree Type",
                              BigDecimal.valueOf(7.5),
                              BigDecimal.valueOf(4.75),
                              BigDecimal.valueOf(3));
    }
    @Override
    DegreeTypeValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception on validateOnCreate() when relevant points are null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenRelevantPointsAreNull() {
        DegreeType degreeType = getModel();
        degreeType.setHighlyRelevantPoints(null);
        degreeType.setLimitedRelevantPoints(null);
        degreeType.setLittleRelevantPoints(null);

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(degreeType));

        assertThat(exception.getReason())
                .contains(List
                        .of("DegreeType.highlyRelevantPoints must not be null.",
                            "DegreeType.limitedRelevantPoints must not be null.",
                            "DegreeType.littleRelevantPoints must not be null."));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when relevant points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenRelevantPointsAreNegative() {
        DegreeType degreeType = getModel();
        degreeType.setHighlyRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLittleRelevantPoints(new BigDecimal("-1.0"));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(degreeType));

        assertThat(exception.getReason())
                .contains(List
                        .of("DegreeType.highlyRelevantPoints must not be negative.",
                            "DegreeType.limitedRelevantPoints must not be negative.",
                            "DegreeType.littleRelevantPoints must not be negative."));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when relevant points are null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenRelevantPointsAreNull() {
        DegreeType degreeType = getModel();
        degreeType.setHighlyRelevantPoints(null);
        degreeType.setLimitedRelevantPoints(null);
        degreeType.setLittleRelevantPoints(null);

        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, degreeType));

        assertThat(exception.getReason())
                .contains(List
                        .of("DegreeType.highlyRelevantPoints must not be null.",
                            "DegreeType.limitedRelevantPoints must not be null.",
                            "DegreeType.littleRelevantPoints must not be null."));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when relevant points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenRelevantPointsAreNegative() {
        DegreeType degreeType = getModel();
        degreeType.setHighlyRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLittleRelevantPoints(new BigDecimal("-1.0"));
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, degreeType));

        assertThat(exception.getReason())
                .contains(List
                        .of("DegreeType.highlyRelevantPoints must not be negative.",
                            "DegreeType.limitedRelevantPoints must not be negative.",
                            "DegreeType.littleRelevantPoints must not be negative."));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenNameAlreadyExists() {
        DegreeType degreeType = getModel();

        when(persistenceService.getByName(degreeType.getName())).thenReturn(Optional.of(new DegreeType()));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(degreeType));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw Exception on validateOnUpdate() when name already exists")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenNameAlreadyExists() {
        Long id = 1L;
        DegreeType degreeType = getModel();
        DegreeType newDegreeType = getModel();
        degreeType.setId(2L);

        when(persistenceService.getByName(newDegreeType.getName())).thenReturn(Optional.of(degreeType));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, newDegreeType));

        assertEquals("Name already exists", exception.getReason());
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}