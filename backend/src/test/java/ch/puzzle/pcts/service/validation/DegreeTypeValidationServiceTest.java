package ch.puzzle.pcts.service.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeTypeValidationServiceTest extends ValidationBaseServiceTest<DegreeType, DegreeTypeValidationService> {

    @InjectMocks
    DegreeTypeValidationService service;

    @Override
    DegreeType getModel() {
        return new DegreeType(null,
                              "Degree Type",
                              BigDecimal.valueOf(7.5),
                              BigDecimal.valueOf(4.75),
                              BigDecimal.valueOf(3));
    }

    @Override
    void validate() {

    }

    @Override
    DegreeTypeValidationService getService() {
        return service;
    }

    @DisplayName("Should throw exception on validateOnCreate() when relevant points are null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenRelevantPointsAreNull() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(degreeType));

        assertThat(exception.getReason())
                .contains(List
                        .of("DegreeType.highlyRelevantPoints must not be null",
                            "DegreeType.limitedRelevantPoints must not be null",
                            "DegreeType.littleRelevantPoints must not be null"));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnCreate() when relevant points are negative")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenRelevantPointsAreNegative() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");
        degreeType.setHighlyRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLimitedRelevantPoints(new BigDecimal("-1.0"));
        degreeType.setLittleRelevantPoints(new BigDecimal("-1.0"));

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnCreate(degreeType));

        assertThat(exception.getReason())
                .contains(List
                        .of("DegreeType.highlyRelevantPoints must not be negative",
                            "DegreeType.limitedRelevantPoints must not be negative",
                            "DegreeType.littleRelevantPoints must not be negative"));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }

    @DisplayName("Should throw exception on validateOnUpdate() when relevant points are null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenRelevantPointsAreNull() {
        DegreeType degreeType = new DegreeType();
        degreeType.setName("Valid Degree type");
        Long id = 1L;

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, degreeType));

        assertThat(exception.getReason())
                .contains(List
                        .of("DegreeType.highlyRelevantPoints must not be null",
                            "DegreeType.limitedRelevantPoints must not be null",
                            "DegreeType.littleRelevantPoints must not be null"));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
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

        PCTSException exception = assertThrows(PCTSException.class, () -> service.validateOnUpdate(id, degreeType));

        assertThat(exception.getReason())
                .contains(List
                        .of("DegreeType.highlyRelevantPoints must not be negative",
                            "DegreeType.limitedRelevantPoints must not be negative",
                            "DegreeType.littleRelevantPoints must not be negative"));
        assertEquals(ErrorKey.INVALID_ARGUMENT, exception.getErrorKey());
    }
}