package ch.puzzle.pcts.service.business;

import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.service.persistence.DegreePersistenceService;
import ch.puzzle.pcts.service.validation.DegreeValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DegreeBusinessService extends BusinessBase<Degree> {

    public DegreeBusinessService(DegreeValidationService validationService,
                                 DegreePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }
    public List<Degree> getAll() {
        return persistenceService.getAll();
    }
}
