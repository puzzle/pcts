package ch.puzzle.pcts.util.validation;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PCTSStringValidationTest extends CustomValidationTestBase {
    DummyValidationService service = new DummyValidationService();

    @DisplayName("Should throw error when string is null")
    @Test
    void shouldThrowErrorWhenStringIsNull() {
        DummyClass invalid = withNonNullableString(null);
        invalid.setBlankAllowedString("Not blank");
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));
        assertEquals(List.of(Map.of(FieldKey.FIELD, "nonNullableString", FieldKey.CLASS, "DummyClass")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw error when string is blank")
    @ParameterizedTest
    @ValueSource(strings = { "", "  " })
    void shouldThrowErrorWhenStringIsBlank(String blank) {
        DummyClass invalid = withNonNullableString(blank);
        invalid.setBlankAllowedString("Not blank");
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals(List.of(Map.of(FieldKey.FIELD, "nonNullableString", FieldKey.CLASS, "DummyClass")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw error when string is too short")
    @ParameterizedTest
    @ValueSource(strings = { "S", "  S " })
    void shouldThrowErrorWhenStringIsTooShort(String shortString) {
        DummyClass invalid = withNonNullableString(shortString);
        invalid.setBlankAllowedString("Not blank");
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "nonNullableString",
                            FieldKey.CLASS,
                            "DummyClass",
                            FieldKey.MIN,
                            "2",
                            FieldKey.MAX,
                            "250",
                            FieldKey.IS,
                            invalid.nonNullableString)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw error when string is too long")
    @Test
    void shouldThrowErrorWhenStringIsTooLong() {
        String longString = "S".repeat(251);
        DummyClass invalid = withNonNullableString(longString);
        invalid.setBlankAllowedString("Not blank");
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals(List
                .of(Map
                        .of(FieldKey.FIELD,
                            "nonNullableString",
                            FieldKey.CLASS,
                            "DummyClass",
                            FieldKey.MIN,
                            "2",
                            FieldKey.MAX,
                            "250",
                            FieldKey.IS,
                            invalid.nonNullableString)),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should not throw error when string is valid")
    @Test
    void shouldNotThrowErrorWhenStringIsValid() {
        DummyClass valid = withNonNullableString("This is composed of valid characters");
        valid.setBlankAllowedString("Not blank");
        service.validate(valid);

    }

    @DisplayName("Should not throw error when string is null and is configured as nullable")
    @Test
    void shouldntThrowErrorWhenStringIsNullAndConfiguredAsNullable() {
        DummyClass valid = withNullableString();
        valid.setBlankAllowedString("Not blank");
        assertDoesNotThrow(() -> service.validate(valid));
    }

    @DisplayName("Should not throw error when string is blank and is configured to allow blank strings")
    @Test
    void shouldntThrowErrorWhenStringIsNullAndConfiguredToAllowBlankStrings() {
        DummyClass valid = withBlankableString();
        assertDoesNotThrow(() -> service.validate(valid));
    }
}
