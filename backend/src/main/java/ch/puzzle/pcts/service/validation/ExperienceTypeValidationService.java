package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.experiencetype.ExperienceType;
import ch.puzzle.pcts.service.persistence.ExperienceTypePersistenceService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExperienceTypeValidationService extends ValidationBase<ExperienceType> {
    private final ExperienceTypePersistenceService persistenceService;

    public ExperienceTypeValidationService(ExperienceTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(ExperienceType experienceType) {
        super.validateOnCreate(experienceType);
        validateNameUniqueness(experienceType.getName());
    }

    @Override
    public void validateOnUpdate(Long id, ExperienceType experienceType) {
        super.validateOnUpdate(id, experienceType);
        validateNameUniqueExcludingSelf(id, experienceType.getName());
    }

    private void validateNameUniqueness(String name) {
        persistenceService.getByName(name).ifPresent(experienceType -> {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        });
    }

    private void validateNameUniqueExcludingSelf(Long id, String name) {
        Optional<ExperienceType> existingOrganisationUnit = persistenceService.getByName(name);
        existingOrganisationUnit.ifPresent(experienceType -> {
            if (!experienceType.getId().equals(id)) {
                throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
            }
        });
    }
}
