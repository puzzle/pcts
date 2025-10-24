package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degreetype.DegreeType;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.service.persistence.DegreeTypePersistenceService;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeTypeValidationService extends ValidationBase<DegreeType> {
    private final DegreeTypePersistenceService persistenceService;

    public DegreeTypeValidationService(DegreeTypePersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnCreate(DegreeType degreeType) {
        super.validateOnCreate(degreeType);
        validateNameUniqueness(degreeType.getName());
    }

    @Override
    public void validateOnUpdate(Long id, DegreeType degreeType) {
        super.validateOnUpdate(id, degreeType);
        validateNameUniqueExcludingSelf(id, degreeType.getName());
    }

    private void validateNameUniqueness(String name) {
        persistenceService.getByName(name).ifPresent(degreeType -> {
            throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
        });
    }

    private void validateNameUniqueExcludingSelf(Long id, String name) {
        Optional<DegreeType> existingOrganisationUnit = persistenceService.getByName(name);
        existingOrganisationUnit.ifPresent(degreeType -> {
            if (!degreeType.getId().equals(id)) {
                throw new PCTSException(HttpStatus.BAD_REQUEST, "Name already exists", ErrorKey.INVALID_ARGUMENT);
            }
        });
    }
}
