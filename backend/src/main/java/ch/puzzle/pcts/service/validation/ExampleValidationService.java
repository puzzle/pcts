package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.example.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Validation services should be used to validate business needs, not simple
 * things that can be done with an @NotNull annotation
 */
@Component
public class ExampleValidationService {
    public void validateOnCreate(Example example) {

        if (!example.getText().contains("Example")) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Text does need to include 'Example'",
                                    ErrorKey.VALIDATION_DOES_NOT_INCLUDE);
        } else if (example.getId() != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.ID_IS_NOT_NULL);
        }
        // with the persistenceService, things like duplications and the like can also
        // be checked
    }
}