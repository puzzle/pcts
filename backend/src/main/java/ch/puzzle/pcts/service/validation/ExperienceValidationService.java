package ch.puzzle.pcts.service.validation;

import static ch.puzzle.pcts.Constants.EXPERIENCE;

import ch.puzzle.pcts.exception.PCTSException;
import ch.puzzle.pcts.model.error.ErrorKey;
import ch.puzzle.pcts.model.error.FieldKey;
import ch.puzzle.pcts.model.experience.Experience;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ExperienceValidationService extends ValidationBase<Experience> {

    @Override
    public void validateOnCreate(Experience experience) {
        super.validateOnCreate(experience);
        validateStartDateIsBeforeEndDate(experience.getStartDate(), experience.getEndDate());
    }

    @Override
    public void validateOnUpdate(Long id, Experience experience) {
        super.validateOnUpdate(id, experience);
        validateStartDateIsBeforeEndDate(experience.getStartDate(), experience.getEndDate());
    }

    public void validateStartDateIsBeforeEndDate(LocalDate startDate, LocalDate endDate) {
        if (endDate != null && startDate.isAfter(endDate)) {
            throw new PCTSException(HttpStatus.BAD_REQUEST,
                                    ErrorKey.ATTRIBUTE_NOT_BEFORE,
                                    Map
                                            .of(FieldKey.ENTITY,
                                                EXPERIENCE,
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
