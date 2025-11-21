package ch.puzzle.pcts.service.validation;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.degree.Degree;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DegreeValidationService extends ValidationBase<Degree> {
    @Override
    public void validateOnCreate(Degree degree) {
        super.validateOnCreate(degree);
        validateStartDateIsBeforeEndDate(degree.getStartDate(), degree.getEndDate());
    }

    @Override
    public void validateOnUpdate(Long id, Degree degree) {
        super.validateOnUpdate(id, degree);
        validateStartDateIsBeforeEndDate(degree.getStartDate(), degree.getEndDate());
    }

    public void validateStartDateIsBeforeEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    ErrorKey.ATTRIBUTE_NOT_BEFORE,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                "degree",
                                                FieldKey.FIELD,
                                                "startDate",
                                                FieldKey.IS,
                                                startDate.toString(),
                                                FieldKey.CONDITION_FIELD,
                                                "endDate",
                                                FieldKey.MAX,
                                                endDate.toString()));
        }
    }
}
