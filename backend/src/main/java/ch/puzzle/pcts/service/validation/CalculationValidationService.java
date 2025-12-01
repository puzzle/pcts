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
    public void validateOnUpdate(Long id, Calculation calculation) {
        super.validateOnUpdate(id, calculation);
        if (calculation.getState().equals(CalculationState.ACTIVE)) {
            validateUserOnlyHasOneActiveCalculationPerRole(calculation, id);
        }
    }

    @Override
    public void validateOnCreate(Calculation calculation) {
        super.validateOnCreate(calculation);
        if (calculation.getState().equals(CalculationState.ACTIVE)) {
            validateUserOnlyHasOneActiveCalculationPerRole(calculation, null);
        }
    }

    public void validateUserOnlyHasOneActiveCalculationPerRole(Calculation calculation, Long id) {
        List<Calculation> calculations = persistenceService
                .getAllByMemberIdAndRoleIdAndState(calculation.getMember().getId(),
                                                   calculation.getRole().getId(),
                                                   CalculationState.ACTIVE);
        if (!calculations.isEmpty() && (id == null || !id.equals(calculations.getFirst().getId()))) {
            Map<FieldKey, String> attributes = Map
                    .of(FieldKey.ENTITY,
                        CALCULATION,
                        FieldKey.FIELD,
                        "member",
                        FieldKey.IS,
                        CalculationState.ACTIVE.toString());

            GenericErrorDto error = new GenericErrorDto(ErrorKey.INVALID_ARGUMENT, attributes);

            throw new PCTSException(HttpStatus.BAD_REQUEST, List.of(error));
        }
    }
}
