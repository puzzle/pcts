package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.CALCULATION;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.dto.error.GenericErrorDto;
import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.calculation.Calculation;
import ch.puzzle.pcts.model.calculation.CalculationState;
import ch.puzzle.pcts.service.persistence.CalculationPersistenceService;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CalculationValidationService extends ValidationBase<Calculation> {
    private final CalculationPersistenceService persistenceService;

    public CalculationValidationService(CalculationPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void validateOnUpdate(Long id, Calculation model) {
        super.validateOnUpdate(id, model);
        CalculationState oldModelState = persistenceService.getById(id).getState();

        if (oldModelState == CalculationState.ARCHIVED) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY, CALCULATION, FieldKey.FIELD, "state", FieldKey.IS, oldModelState.name());

            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    List.of(new GenericErrorDto(ErrorKey.UPDATE_ARCHIVED_CALCULATION, attributes)));
        }
    }
}
