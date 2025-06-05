package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.dto.CreateExampleDto;
import ch.puzzle.pcts.exception.PctsResponseStatusException;
import ch.puzzle.pcts.service.persistence.ExamplePersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Validation services should be used to validate business needs, not simple things that can be done with an @NotNull annotation
 */
@Component
public class ExampleValidationService {
    private final ExamplePersistenceService persistenceService;

    @Autowired
    public ExampleValidationService(ExamplePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public void validateOnCreate(CreateExampleDto dto){
        if(!dto.text().contains("Example")){
            throw new PctsResponseStatusException(HttpStatus.BAD_REQUEST, "Text does at need to include 'Example'");
        }
        // with the persistenceService, things like duplications and the like can also be checked
    }
}