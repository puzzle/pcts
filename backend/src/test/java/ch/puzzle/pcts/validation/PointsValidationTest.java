package ch.puzzle.pcts.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.service.validation.ValidationBase;
import ch.puzzle.pcts.validation.points.PointsValidation;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointsValidationTest {
    DummyValidationService service = new DummyValidationService();

    @DisplayName("Should throw error when points are null")
    @Test
    void shouldThrowErrorWhenPointsIsNull() {
        DummyClass invalid = new DummyClass(null);
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals("DummyClass.bigDecimal must not be null.", exception.getReason());
    }

    @DisplayName("Should throw error when points are negative")
    @Test
    void shouldThrowErrorWhenPointsAreNegative() {
        DummyClass invalid = new DummyClass(new BigDecimal("-1"));
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals("DummyClass.bigDecimal must not be negative.", exception.getReason());
    }

    @DisplayName("Should not throw error when points are valid")
    @Test
    void shouldNotThrowErrorWhenPointsAreValid() {
        DummyClass valid = new DummyClass(new BigDecimal("1"));
        service.validate(valid);
    }

    private static class DummyClass implements Model {
        @PointsValidation
        BigDecimal bigDecimal;

        public DummyClass(BigDecimal bigDecimal) {
            this.bigDecimal = bigDecimal;
        }

        @Override
        public Long getId() {
            return 0L;
        }

        @Override
        public void setId(Long id) {
            // Isn't needed for the tests
        }
    }

    private static class DummyValidationService extends ValidationBase<DummyClass> {
        public DummyValidationService() {
            super();
        }
    }
}
