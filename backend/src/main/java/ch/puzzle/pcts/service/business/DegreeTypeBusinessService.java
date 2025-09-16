package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeTypeValidationService;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypeBusinessService {
    private final DegreeTypeValidationService validationService;
    private final DegreeTypePersistenceService persistenceService;

    public DegreeTypeBusinessService(DegreeTypeValidationService validationService,
                                     DegreeTypePersistenceService persistenceService) {
        this.validationService = validationService;
        this.persistenceService = persistenceService;
    }
}
