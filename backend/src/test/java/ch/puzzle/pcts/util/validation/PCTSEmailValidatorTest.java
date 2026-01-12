package ch.puzzle.pcts.util.validation;

import static ch.puzzle.pcts.util.validation.CustomValidationTestBase.withEmail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PCTSEmailValidatorTest {
    CustomValidationTestBase.DummyValidationService service = new CustomValidationTestBase.DummyValidationService();

    @DisplayName("Should throw error when email is blank")
    @ParameterizedTest
    @ValueSource(strings = { "", "  " })
    void shouldThrowErrorWhenStringIsBlank(String blank) {
        CustomValidationTestBase.DummyClass invalid = withEmail(blank);
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals(List.of(Map.of(FieldKey.FIELD, "email", FieldKey.CLASS, "DummyClass")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should throw error when email is not valid")
    @ParameterizedTest
    @ValueSource(strings = { "a", "a.a", "a@.", "a.com" })
    void shouldThrowErrorWhenEmailIsInvalid(String email) {
        CustomValidationTestBase.DummyClass invalid = withEmail(email);
        PCTSException exception = assertThrows(PCTSException.class, () -> service.validate(invalid));

        assertEquals(List.of(Map.of(FieldKey.IS, email, FieldKey.FIELD, "email", FieldKey.CLASS, "DummyClass")),
                     exception.getErrorAttributes());
    }

    @DisplayName("Should not throw error when email is null")
    @Test
    void shouldNotThrowErrorWhenStringIsNull() {
        CustomValidationTestBase.DummyClass valid = withEmail(null);
        service.validate(valid);
    }

    @DisplayName("Should not throw error when email is valid")
    @Test
    void shouldNotThrowErrorWhenStringIsValid() {
        CustomValidationTestBase.DummyClass valid = withEmail("email@example.com");
        service.validate(valid);
    }
}