package ch.puzzle.pcts.util.validation;

import static org.apache.commons.lang3.StringUtils.trim;

import ch.puzzle.pcts.model.Model;
import ch.puzzle.pcts.service.validation.ValidationBase;

public class CustomValidationTestBase {
    private static DummyClass createValid() {
        DummyClass dummy = new DummyClass();
        dummy.setEmail("email@example.com");
        dummy.setString("This is a valid string");
        return dummy;
    }

    protected static DummyClass withEmail(String email) {
        DummyClass dummy = createValid();
        dummy.setEmail(email);
        return dummy;
    }

    protected static DummyClass withString(String string) {
        DummyClass dummy = createValid();
        dummy.setString(string);
        return dummy;
    }

    protected static class DummyClass implements Model {
        @PCTSStringValidation
        String string;

        @PCTSEmailValidation
        String email;

        public void setEmail(String email) {
            this.email = trim(email);
        }

        public void setString(String string) {
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

    protected static class DummyValidationService extends ValidationBase<DummyClass> {
        public DummyValidationService() {
            super();
        }
    }
}
