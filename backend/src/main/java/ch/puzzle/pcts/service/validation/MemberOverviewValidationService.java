package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.service.validation.ValidationBase.buildGenericErrorDto;

import ch.puzzle.pcts.dto.error.ErrorKey;
import ch.puzzle.pcts.dto.error.FieldKey;
import ch.puzzle.pcts.exception.PCTSException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MemberOverviewValidationService {

    public void validateOnGetById(Long id) {
        if (id == null) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    buildGenericErrorDto(ErrorKey.VALIDATION, Map.of(FieldKey.FIELD, "id")));
        }
    }
}
