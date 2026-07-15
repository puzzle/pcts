package ch.puzzle.pcts.util.validation;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.service.validation.ValidationBase;

public class CustomValidationTestBase {
    private static DummyClass createValid() {
        DummyClass dummy = new DummyClass();
        dummy.setNonNullableString("This is a valid string");
        return dummy;
    }

    private static DummyClass withNonNullableString(String string) {
        DummyClass dummy = createValid();
        dummy.setNonNullableString(string);
        return dummy;
    }

    private static DummyClass withNullableString() {
        DummyClass dummy = createValid();
        dummy.setNullableString(null);
        return dummy;
    }

    private static DummyClass withBlankableString() {
        DummyClass dummy = createValid();
        dummy.setBlankAllowedString("");
        return dummy;
    }

    protected static DummyClass createNonNullable(String string) {
        return withNonNullableString(string);
    }

    protected static DummyClass createNullable() {
        return withNullableString();
    }

    protected static DummyClass createOnlyWhiteSpacesAllowed() {
        return withBlankableString();
    }

    protected static class DummyClass implements Model {
        @PCTSStringValidation
        String nonNullableString;

        @PCTSStringValidation(nullable = true)
        String nullableString;

        @PCTSStringValidation(allowOnlyWhiteSpaces = true)
        String blankAllowedString;

        public void setNonNullableString(String nonNullableString) {
            this.nonNullableString = trim(nonNullableString);
        }

        public void setNullableString(String nullableString) {
            this.nullableString = trim(nullableString);
        }

        public void setBlankAllowedString(String blankAllowedString) {
            this.blankAllowedString = trim(blankAllowedString);
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

    protected static class DummyValidationService extends ValidationBase<DummyClass> {
        public DummyValidationService() {
            super();
        }
    }
}
