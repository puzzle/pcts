package ch.puzzle.pcts.util;

import static org.apache.commons.lang3.StringUtils.trim;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.service.validation.ValidationBase;
import ch.puzzle.pcts.util.validation.PCTSStringValidation;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PCTSStringValidationTest {
    DummyValidationService service = new DummyValidationService();

    @DisplayName("Should throw error when string is null")
    @Test
    void shouldThrowErrorWhenStringIsNull() {
        DummyClass invalid = new DummyClass(null);
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));
        assertEquals(List.of(Map.of(FieldKey.FIELD, "string", FieldKey.CLASS, "DummyClass")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw error when string is blank")
    @ParameterizedTest
    @ValueSource(strings = { "", "  " })
    void shouldThrowErrorWhenStringIsBlank(String blank) {
        DummyClass invalid = new DummyClass(blank);
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals(List.of(Map.of(FieldKey.FIELD, "string", FieldKey.CLASS, "DummyClass")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw error when string is too short")
    @ParameterizedTest
    @ValueSource(strings = { "S", "  S " })
    void shouldThrowErrorWhenStringIsTooShort(String shortString) {
        DummyClass invalid = new DummyClass(shortString);
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "string",
                            FieldKey.CLASS,
                            "DummyClass",
                            FieldKey.MIN,
                            "2",
                            FieldKey.MAX,
                            "250",
                            FieldKey.IS,
                            invalid.string)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw error when string is too long")
    @Test
    void shouldThrowErrorWhenStringIsTooLong() {
        String longString = "S".repeat(251);
        DummyClass invalid = new DummyClass(longString);
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "string",
                            FieldKey.CLASS,
                            "DummyClass",
                            FieldKey.MIN,
                            "2",
                            FieldKey.MAX,
                            "250",
                            FieldKey.IS,
                            invalid.string)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should not throw error when string is valid")
    @Test
    void shouldNotThrowErrorWhenStringIsValid() {
        DummyClass valid = new DummyClass("This is composed of valid characters");
        service.validate(valid);
    }

    private static class DummyClass implements Model {
        @PCTSStringValidation
        String string;

        public DummyClass(String string) {
            this.string = trim(string);
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
