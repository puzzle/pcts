package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.DEGREE_TYPE;

import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.repository.DegreeTypeRepository;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeTypeValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypeBusinessService
        extends
            BusinessBase<DegreeType, DegreeTypeValidationService, DegreeTypeRepository, DegreeTypePersistenceService> {

    public DegreeTypeBusinessService(DegreeTypeValidationService validationService,
                                     DegreeTypePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<DegreeType> getAll() {
        return persistenceService.getAll();
    }

    @Override
    protected String entityName() {
        return DEGREE_TYPE;
    }
}
