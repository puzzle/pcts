package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.dto.example.ExampleDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Validation services should be used to validate business needs, not simple
 * things that can be done with an @NotNull annotation
 */
@Component
public class ExampleValidationService {
    private final ExamplePersistenceService persistenceService;

    @Autowired
    public ExampleValidationService(ExamplePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnCreate(ExampleDto dto) {

        if (!dto.text().contains("Example")) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    "Text does need to include 'Example'",
                                    ErrorKey.VALIDATION_DOES_NOT_INCLUDE);
        }
        else if (dto.id() != null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Id needs to be undefined", ErrorKey.VALIDATION_DOES_NOT_INCLUDE);
        }
        // with the persistenceService, things like duplications and the like can also
        // be checked
    }
}