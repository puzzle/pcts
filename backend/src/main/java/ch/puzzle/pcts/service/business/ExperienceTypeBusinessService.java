package ch.puzzle.pcts.service.business;

import static ch.puzzle.pcts.Constants.EXPERIENCE_TYPE;

import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.repository.ExperienceTypeRepository;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import ch.puzzle.pcts.service.validation.ExperienceTypeValidationService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypeBusinessService
        extends
            BusinessBase<ExperienceType, ExperienceTypeRepository, ExperienceTypeValidationService, ExperienceTypePersistenceService> {

    public ExperienceTypeBusinessService(ExperienceTypeValidationService validationService,
                                         ExperienceTypePersistenceService persistenceService) {
        super(validationService, persistenceService);
    }

    public List<ExperienceType> getAll() {
        return persistenceService.getAll();
    }

    @Override
    protected String entityName() {
        return EXPERIENCE_TYPE;
    }
}
